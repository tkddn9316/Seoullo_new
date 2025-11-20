package com.app.data.repository

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import com.app.domain.repository.ImageSaverRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ImageSaverRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageSaverRepository {

    override fun saveImageToGallery(
        url: String,
        displayName: String?,
        subDir: String
    ): Flow<Uri> = callbackFlow {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val fileName = buildOutputFileName(displayName, url)
        val mimeType = mimeForExtension(fileName.substringAfterLast('.', "").lowercase(Locale.ROOT))

        val request = DownloadManager.Request(Uri.parse(url))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .apply { if (mimeType.isNotEmpty()) setMimeType(mimeType) }
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "$subDir/$fileName")
        val id = downloadManager.enqueue(request)

        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action != DownloadManager.ACTION_DOWNLOAD_COMPLETE) return
                val doneId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
                if (doneId != id) return

                runCatching { context.unregisterReceiver(this) }

                val query = DownloadManager.Query().setFilterById(id)
                downloadManager.query(query).use { cursor ->
                    if (cursor.moveToFirst()) {
                        val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            val mediaCol = cursor.getColumnIndex(DownloadManager.COLUMN_MEDIAPROVIDER_URI)
                            val localCol = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                            val uriStr = when {
                                mediaCol >= 0 -> cursor.getString(mediaCol)
                                localCol >= 0 -> cursor.getString(localCol)
                                else -> null
                            }
                            if (uriStr != null) {
                                trySend(Uri.parse(uriStr)).isSuccess
                                close()
                            } else {
                                close(IOException("Downloaded but uri is null"))
                            }
                        } else {
                            val reason = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON))
                            close(IOException("Download failed: $reason"))
                        }
                    } else {
                        close(IOException("Query failed"))
                    }
                }
            }
        }

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            ContextCompat.RECEIVER_EXPORTED else 0
        ContextCompat.registerReceiver(context, receiver, filter, flags)

        awaitClose { runCatching { context.unregisterReceiver(receiver) } }
    }

    /** displayName이 확장자를 포함하면 그대로, 아니면 URL에서 확장자를 추출해 붙임. 없으면 jpg 기본값 */
    private fun buildOutputFileName(
        displayName: String?,
        url: String
    ): String {
        if (!displayName.isNullOrBlank()) {
            val hasExt = displayName.contains('.')
            return if (hasExt) {
                displayName
            } else {
                val ext = inferExtensionFromUrl(url)
                if (ext.isNotEmpty()) "$displayName.$ext" else displayName
            }
        }
        val base = "IMG_${System.currentTimeMillis()}"
        val ext = inferExtensionFromUrl(url).ifEmpty { "jpg" }
        return "$base.$ext"
    }

    /** URL path에서 확장자를 추출(대소문자 무시). 없으면 "" */
    private fun inferExtensionFromUrl(url: String): String {
        val lower = url.lowercase(Locale.ROOT)
        val regex = Regex(
            """\.(jpg|jpeg|png|webp|gif|bmp|heic|heif|avif)(?=($|[?#]))""",
            RegexOption.IGNORE_CASE
        )
        val m = regex.find(lower) ?: return ""
        return m.groupValues[1]
    }

    /** 간단한 확장자→MIME 매핑. */
    private fun mimeForExtension(ext: String): String = when (ext) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "webp" -> "image/webp"
        "gif" -> "image/gif"
        "bmp" -> "image/bmp"
        "heic" -> "image/heic"
        "heif" -> "image/heif"
        "avif" -> "image/avif"
        else -> ""
    }
}