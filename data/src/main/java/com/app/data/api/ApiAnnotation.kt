package com.app.data.api

import javax.inject.Qualifier

object ApiAnnotation {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class GooglePlacesApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class GoogleMapsApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class TourApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OpenWeatherApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SeoulOpenApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SeoulSunriseApi
}