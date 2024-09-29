package com.app.data.api

import javax.inject.Qualifier

object ApiAnnotation {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class GoogleApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class TourApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OpenWeatherApi
}