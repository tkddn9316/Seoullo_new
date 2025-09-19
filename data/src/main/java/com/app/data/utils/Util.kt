package com.app.data.utils

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

object Util {
    fun String.addHttps(): String {
        return if (this.startsWith("http://") || this.startsWith("https://")) this
        else "https://$this"
    }

    fun splitCategory(input: String): Triple<String, String, String> {
        return when {
            input.length >= 9 -> Triple(input.substring(0, 3), input.substring(0, 5), input)
            input.length >= 5 -> Triple(input.substring(0, 3), input.substring(0, 5), "")
            input.length >= 3 -> Triple(input.substring(0, 3), "", "")
            else -> Triple("", "", "")
        }
    }

    /** 단일 문서 실시간 */
    fun <T> DocumentReference.asSnapshotFlowSingle(
        mapper: (doc: DocumentSnapshot) -> T?
    ): Flow<T?> = callbackFlow {
        val reg: ListenerRegistration = addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            trySend(snapshot?.let(mapper)).isSuccess
        }
        awaitClose { reg.remove() }
    }

    /** 컬렉션/쿼리 실시간 */
    fun <T> Query.asSnapshotFlowList(
        mapper: (doc: DocumentSnapshot) -> T?
    ): Flow<List<T>> = callbackFlow {
        val reg: ListenerRegistration = addSnapshotListener { qs: QuerySnapshot?, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val list = qs?.documents?.mapNotNull(mapper).orEmpty()
            trySend(list).isSuccess
        }
        awaitClose { reg.remove() }
    }
}