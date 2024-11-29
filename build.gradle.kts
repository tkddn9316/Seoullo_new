// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.7.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20"
    id("com.android.library") version "8.7.2" apply false
    alias(libs.plugins.android.hilt) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    alias(libs.plugins.auto.license).version(libs.versions.autoLicense) apply false
    kotlin("plugin.serialization") version "2.0.20"
}