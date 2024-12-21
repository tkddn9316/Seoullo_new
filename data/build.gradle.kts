plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
//    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")

    alias(libs.plugins.android.hilt)
}

android {
    namespace = "com.app.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 31

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Hilt
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    // Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.okhttp)
    implementation( libs.logging.interceptor)

    // Room
    implementation(libs.room.runtime)
    kapt (libs.room.compiler)
    // Kotlin Extensions and Coroutines support for Room
    implementation (libs.room.ktx)

    // paging
    implementation(libs.androidx.paging.runtime.ktx)

    // DataStore
    implementation(libs.androidx.datastore.preferences)
}