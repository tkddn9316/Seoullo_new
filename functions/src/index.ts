/* eslint-disable require-jsdoc */
import {
  onCall,
  HttpsError,
} from "firebase-functions/v2/https";
import {setGlobalOptions} from "firebase-functions/v2/options";
import * as admin from "firebase-admin";
import * as logger from "firebase-functions/logger";
import {
  getFirestore,
  CollectionReference,
  DocumentReference,
  BulkWriter,
} from "firebase-admin/firestore";
import type {BulkWriterError} from "@google-cloud/firestore";
import {getStorage} from "firebase-admin/storage";

if (!admin.apps.length) {
  admin.initializeApp({
    storageBucket: "seoullo-new.appspot.com",
  });
}

setGlobalOptions({
  region: "asia-northeast3",
  timeoutSeconds: 300,
  memory: "512MiB",
  maxInstances: 10,
});

const db = getFirestore(admin.app(), "seoullo-places-review-database");

type DeletePostRequest = { postId: string };
type DeletePostResult = { deletedDocs: number; deletedFiles: number };

async function deleteCollectionRecursively(
  colRef: CollectionReference,
  writer: BulkWriter,
  pageSize = 500
): Promise<number> {
  let deleted = 0;
  let hasMore = true;

  while (hasMore) {
    const snap = await colRef.limit(pageSize).get();
    if (snap.empty) break;

    for (const d of snap.docs) {
      const subs = await d.ref.listCollections();
      for (const sub of subs) {
        deleted += await deleteCollectionRecursively(sub, writer, pageSize);
      }
      writer.delete(d.ref);
      deleted++;
    }

    // 다음 페이지가 더 있는지 여부로 갱신
    hasMore = snap.size >= pageSize;
  }
  return deleted;
}


async function deleteDocumentRecursively(
  docRef: DocumentReference,
  writer: BulkWriter
): Promise<number> {
  let deleted = 0;
  try {
    const subs = await docRef.listCollections().catch((e: unknown) => {
      logger.warn("[deletePost] listCollections failed (possibly deleted)", {
        path: docRef.path,
        err: (e as Error)?.message,
      });
      return [] as CollectionReference[];
    });

    for (const sub of subs) {
      deleted += await deleteCollectionRecursively(sub, writer);
    }

    writer.delete(docRef);
    deleted++;
  } catch (e: unknown) {
    logger.warn("[deletePost] deleteDocumentRecursively catch", {
      path: docRef.path,
      err: (e as Error)?.message ?? String(e),
    });
  }
  return deleted;
}

async function deleteStorageByPrefix(prefix: string): Promise<number> {
  const bucket = getStorage().bucket(); // 기본 버킷 (위에서 initializeApp에 명시)
  const [exists] = await bucket.exists();
  if (!exists) {
    logger.warn("[deletePost] bucket not found", {bucket: bucket.name});
    return 0;
  }

  const [files] = await bucket.getFiles({prefix});
  if (!files.length) return 0;

  // for-of 사용으로 암시적 any 회피
  let count = 0;
  for (const f of files) {
    try {
      await f.delete();
      count++;
    } catch (e: unknown) {
      logger.warn("[deletePost] file delete failed", {
        name: f.name,
        err: (e as Error)?.message,
      });
    }
  }
  return count;
}

function attachBulkWriterLogging(writer: BulkWriter) {
  writer.onWriteError((err: BulkWriterError) => {
    logger.error("[deletePost] BulkWriter error", {
      code: err.code,
      message: err.message,
      path: err.documentRef.path,
      attempts: err.failedAttempts,
    });
    if (err.code === 5 /* NOT_FOUND */) return false; // 경합으로 이미 삭제된 경우 무시
    return err.failedAttempts < 3; // 그 외엔 최대 3회 재시도
  });
}

export const deletePost = onCall(async (req) => {
  const data = req.data as DeletePostRequest;
  const uid = req.auth?.uid;
  const postId = data?.postId;

  if (!uid) throw new HttpsError("unauthenticated", "Sign-in required.");
  if (!postId) throw new HttpsError("invalid-argument", "postId is required.");

  logger.info("[deletePost] start", {uid, postId});

  const postRef = db.collection("posts").doc(postId);
  const postSnap = await postRef.get();
  if (!postSnap.exists) {
    logger.info("[deletePost] post not found", {postId});
    return {deletedDocs: 0, deletedFiles: 0} as DeletePostResult;
  }

  const authorId = postSnap.get("authorId");
  const token = req.auth?.token;
  const isAdmin = token?.admin === true || token?.role === "admin";
  if (authorId !== uid && !isAdmin) {
    throw new HttpsError(
      "permission-denied",
      "Only author or admin can delete this post."
    );
  }

  const writer = db.bulkWriter();
  attachBulkWriterLogging(writer);

  let deletedDocs = 0;
  try {
    deletedDocs = await deleteDocumentRecursively(postRef, writer);
    await writer.close();
    logger.info(
      "[deletePost] firestore deleted",
      {deletedDocs}
    );
  } catch (e: unknown) {
    logger.error("[deletePost] firestore delete failed", e);
    throw new HttpsError(
      "internal",
      `Firestore delete failed: ${(e as Error)?.message ?? e}`
    );
  }

  let deletedFiles = 0;
  try {
    deletedFiles = await deleteStorageByPrefix(`posts/${postId}/`);
    logger.info("[deletePost] storage deleted", {deletedFiles});
  } catch (e: unknown) {
    logger.error("[deletePost] storage delete failed (ignored)", e);
    // 실패해도 전체 실패로 만들지 않음
  }

  logger.info("[deletePost] done", {postId, deletedDocs, deletedFiles});
  return {deletedDocs, deletedFiles} as DeletePostResult;
});
