// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.hilt) apply false
    alias(libs.plugins.gms.google.services) apply false
    alias(libs.plugins.auto.license).version(libs.versions.autoLicense) apply false
    kotlin(libs.plugins.kotlin.serialization.get().pluginId).version(libs.versions.kotlin).apply(false)
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.google.maps.sdk.screct) apply false
}