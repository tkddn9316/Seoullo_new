import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.android.tools.r8.internal.`in`



plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.gms.google.services)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.auto.license)
    alias(libs.plugins.kotlin.ksp)
    kotlin(libs.plugins.kotlin.serialization.get().pluginId).version(libs.versions.kotlin)
    alias(libs.plugins.google.maps.sdk.screct)

    id("kotlin-kapt")   // 데이터바인딩 때문에 필요함..(추후 제거)
}

android {
    namespace = "com.app.seoullo_new"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.app.seoullo_new"
        minSdk = 31
        targetSdk = 35
        versionCode = 7
        versionName = "1.0.7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // TourApi
        buildConfigField("String", "TOUR_API_KEY", getApiKey("TOUR_API_KEY"))
        // Google Api
        buildConfigField("String", "SEOULLO_GOOGLE_MAPS_API_KEY", getApiKey("SEOULLO_GOOGLE_MAPS_API_KEY"))
        // 서울 공공데이터
        buildConfigField("String", "SEOUL_OPEN_API_KEY", getApiKey("SEOUL_OPEN_API_KEY"))
        // Open Weather Map
        buildConfigField("String","OPEN_WEATHER_MAP_KEY", getApiKey("OPEN_WEATHER_MAP_KEY"))
        // GOOGLE_CLIENT_ID
        buildConfigField("String", "GOOGLE_CLIENT_ID", getApiKey("GOOGLE_CLIENT_ID"))

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    dataBinding {
        enable = true
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kapt {
        correctErrorTypes = true
    }
}

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.activity.ktx)
    implementation(libs.fragment.ktx)
    implementation(libs.preference.ktx)
    implementation(libs.joda.time)

    // Compose
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    debugImplementation(libs.ui.tooling)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.compose.foundation)
    // compose StatusBar Color
    implementation(libs.accompanist.systemuicontroller)
    // compose permission
    implementation(libs.accompanist.permission)

    // Glide
    implementation(libs.landscapist.glide)

    // 위치 구하기
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)

    // Hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    // Kotlin Extensions and Coroutines support for Room
    implementation(libs.room.ktx)

    // ViewModelScope
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Google Login
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.play.services.auth)

    // google maps
    implementation(libs.maps.compose)
    // Optionally, you can include the Compose utils library for Clustering,
    // Street View metadata checks, etc.
    implementation(libs.maps.compose.utils)
    // Optionally, you can include the widgets library for ScaleBar, etc.
    implementation(libs.maps.compose.widgets)

    // Ted Permission
    implementation(libs.tedpermission.coroutine)

    // navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // paging
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.paging.compose)

    // viewPager, bottomNav
    implementation(libs.androidx.navigation.compose)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)

    // lottie
    implementation(libs.lottie.compose)

    // License page UI
    implementation(libs.auto.license.core)
    implementation(libs.auto.license.ui)

    // kotlin json
    implementation(libs.kotlinx.serialization.json)

    // balloon
    implementation(libs.balloon)

    // in-app-update
    implementation(libs.update)
    implementation(libs.update.ktx)
}