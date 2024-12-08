package com.app.seoullo_new.di

import android.app.Application
import com.app.seoullo_new.BuildConfig
import com.naver.maps.map.NaverMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 네이버 지도 클라이언트 지정하기
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_MAPS_CLIENT_ID)
    }
}