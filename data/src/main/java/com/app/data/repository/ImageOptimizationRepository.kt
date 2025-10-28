package com.app.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.annotation.WorkerThread
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max
import kotlin.math.roundToInt

@Singleton
class ImageOptimizationRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    data class EncodedImage(
        val bytes: ByteArray,
        val contentType: String,    // e.g. "image/jpeg", "image/webp", "image/png"
        val extension: String       // e.g. "jpg", "webp", "png"
    )

    @WorkerThread
    suspend fun decodeAndResizeBitmap(
        uri: Uri,
        maxDim: Int = 2048
    ): Bitmap = withContext(Dispatchers.IO) {
        val cr = context.contentResolver
        val source = ImageDecoder.createSource(cr, uri)
        ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
            val srcW = info.size.width
            val srcH = info.size.height
            val longSide = max(srcW, srcH).toFloat()
            if (longSide > maxDim) {
                val scale = longSide / maxDim
                val dstW = (srcW / scale).roundToInt()
                val dstH = (srcH / scale).roundToInt()
                decoder.setTargetSize(dstW, dstH)
            }

            // 압축을 위해 HW 대신 SW 비트맵 권장
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            decoder.isMutableRequired = false
        }
    }

    @WorkerThread
    fun compressToTarget(
        bitmap: Bitmap,
        targetBytes: Int = 700_000,
        preferWebpOnR30Plus: Boolean = true
    ): EncodedImage {
        val hasAlpha = bitmap.hasAlpha()

        fun compressOnce(format: Bitmap.CompressFormat, quality: Int): ByteArray {
            val bos = ByteArrayOutputStream()
            bitmap.compress(format, quality, bos)
            return bos.toByteArray()
        }

        if (hasAlpha) {
            return if (preferWebpOnR30Plus) {
                val out = compressOnce(Bitmap.CompressFormat.WEBP_LOSSLESS, 100)
                EncodedImage(bytes = out, contentType = "image/webp", extension = "webp")
            } else {
                val out = compressOnce(Bitmap.CompressFormat.PNG, 100)
                EncodedImage(bytes = out, contentType = "image/png", extension = "png")
            }
        } else {
            var q = 92
            var step = 8
            var last = compressOnce(if (preferWebpOnR30Plus) Bitmap.CompressFormat.WEBP_LOSSY else Bitmap.CompressFormat.JPEG, q)
            while (last.size > targetBytes && q > 40) {
                q -= step
                last = compressOnce(if (preferWebpOnR30Plus) Bitmap.CompressFormat.WEBP_LOSSY else Bitmap.CompressFormat.JPEG, q)
                if (q <= 60) step = 4
            }
            val ct = if (preferWebpOnR30Plus) "image/webp" else "image/jpeg"
            val ext = if (preferWebpOnR30Plus) "webp" else "jpg"
            return EncodedImage(last, ct, ext)
        }
    }

    /**
     * 단일 URI를 최적화(리사이즈+압축)해 업로드-ready 바이트로 반환.
     */
    @WorkerThread
    suspend fun optimizeUri(
        uri: Uri,
        maxDim: Int = 2048,
        targetBytes: Int = 700_000,
        preferWebpOnR30Plus: Boolean = true
    ): EncodedImage = withContext(Dispatchers.IO) {
        val bmp = decodeAndResizeBitmap(uri, maxDim)
        compressToTarget(bmp, targetBytes, preferWebpOnR30Plus)
    }
}