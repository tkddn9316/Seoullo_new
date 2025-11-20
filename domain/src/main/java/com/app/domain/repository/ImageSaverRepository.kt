package com.app.domain.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface ImageSaverRepository {
    /**
     * 주어진 이미지 URL을 다운로드해서 갤러리(Pictures/<subDir>)에 저장.
     * @return 저장된 MediaStore Uri
     */
    fun saveImageToGallery(
        url: String,
        displayName: String? = null,
        subDir: String = "Seoullo"
    ): Flow<Uri>
}