package com.app.data.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
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
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore("seoullo-places-review-database")

    //    @Provides
    //    @Singleton
    //    fun provideStorage(): FirebaseStorage = FirebaseStorage.getInstance()
    @Provides
    @Singleton
    fun provideStorage(): FirebaseStorage = Firebase.storage("gs://seoullo-new.firebasestorage.app")

    @Provides
    @Singleton
    @Named("boardRef")
    fun provideBoardRef() =
        Firebase.firestore("seoullo-places-review-database").collection("posts")

    @Provides
    @Singleton
    @Named("reviewsRef")
    fun provideReviewsRef() =
        Firebase.firestore("seoullo-places-review-database").collection("reviews")
}