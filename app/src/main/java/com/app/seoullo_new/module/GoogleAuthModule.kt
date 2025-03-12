package com.app.seoullo_new.module

import android.content.Context
import androidx.credentials.CredentialManager
import com.app.seoullo_new.di.GoogleSignInManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleAuthModule {
    @Provides
    @Singleton
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager {
        return CredentialManager.create(context)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideGoogleSignInManager(
        @ApplicationContext context: Context,
        credentialManager: CredentialManager,
        auth: FirebaseAuth
    ): GoogleSignInManager {
        return GoogleSignInManager(context, credentialManager, auth)
    }
}