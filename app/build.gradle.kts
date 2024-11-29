import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
//    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
//    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("kotlinx-serialization")

    alias(libs.plugins.android.hilt)
    alias(libs.plugins.auto.license)
}

android {
    namespace = "com.app.seoullo_new"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.app.seoullo_new"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "TOUR_API_KEY", getApiKey("TOUR_API_KEY"))
        buildConfigField(
            "String",
            "SEOULLO_GOOGLE_MAPS_API_KEY",
            getApiKey("SEOULLO_GOOGLE_MAPS_API_KEY")
        )
        buildConfigField("String", "SEOUL_OPEN_API_KEY", getApiKey("SEOUL_OPEN_API_KEY"))
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("androidx.fragment:fragment-ktx:1.8.5")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("joda-time:joda-time:2.12.7")

    // Compose
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation(platform("androidx.compose:compose-bom:2024.11.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.11.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.5")
    implementation("androidx.compose.material:material-icons-extended:1.7.5")
    // compose StatusBar Color
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")

    // Glide
    implementation("com.github.skydoves:landscapist-glide:1.4.7")

    // 위치 구하기
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Hilt
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation)

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.6.1")

    // ViewModelScope
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    // Google Login
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // Ted Permission
    implementation("io.github.ParkSangGwon:tedpermission-coroutine:3.3.0")

    // navigation
    val nav_version = "2.8.4"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // paging
    val paging_version = "3.3.4"
    implementation("androidx.paging:paging-runtime-ktx:$paging_version")
    implementation("androidx.paging:paging-compose:$paging_version")

    // viewPager, bottomNav
    implementation ("androidx.navigation:navigation-compose:2.8.4")
    implementation ("com.google.accompanist:accompanist-pager:0.20.1")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.20.1")

    // lottie
    val lottieVersion = "6.4.1"
    implementation ("com.airbnb.android:lottie-compose:$lottieVersion")

    // License page UI
    implementation(libs.auto.license.core)
    implementation(libs.auto.license.ui)

    // kotlin json
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
}