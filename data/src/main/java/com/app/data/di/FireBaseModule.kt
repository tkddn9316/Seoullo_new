package com.app.data.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class FireBaseModule {
    @Provides
    @Singleton
    @Named("reviewsRef")
    fun provideReviewsRef() =
        Firebase.firestore("seoullo-places-review-database").collection("reviews")
}