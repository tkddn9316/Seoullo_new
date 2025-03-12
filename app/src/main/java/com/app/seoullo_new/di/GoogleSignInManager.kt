package com.app.seoullo_new.di

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.app.domain.model.common.ApiState
import com.app.seoullo_new.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleSignInManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val credentialManager: CredentialManager,
    private val auth: FirebaseAuth
) {
    fun checkAutoSignIn(token: String): Flow<ApiState<GoogleIdTokenCredential?>> = flow {
        emit(ApiState.Loading())

        if (token.isNotEmpty()) {
            emit(ApiState.Success(null))
            return@flow
        }

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true) // 기존에 로그인했던 계정만 사용
            .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
            .setAutoSelectEnabled(true) // 자동 선택 활성화
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )
            when (val credential = result.credential) {
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val data = GoogleIdTokenCredential.createFrom(credential.data)

                        // Firebase에 로그인
                        val firebaseCredential = GoogleAuthProvider.getCredential(data.idToken, null)
                        auth.signInWithCredential(firebaseCredential)

                        emit(ApiState.Success(data))
                    } else {
                        emit(ApiState.Error("Invalid credential type"))
                    }
                }
                else -> emit(ApiState.Error("No valid credentials found"))
            }
        } catch (e: GetCredentialException) {
            emit(ApiState.Error(e.message ?: ""))
        }
    }

    fun signIn(): Flow<ApiState<GoogleIdTokenCredential?>> = flow {
        emit(ApiState.Loading())

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )
            when (val credential = result.credential) {
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val data = GoogleIdTokenCredential.createFrom(credential.data)

                        // Firebase에 로그인
                        val firebaseCredential = GoogleAuthProvider.getCredential(data.idToken, null)
                        auth.signInWithCredential(firebaseCredential)

                        emit(ApiState.Success(data))
                    } else {
                        emit(ApiState.Error("Invalid credential type"))
                    }
                }
                else -> emit(ApiState.Error("No valid credentials found"))
            }
        } catch (e: GetCredentialException) {
            emit(ApiState.Error(e.message ?: ""))
        }
    }

    suspend fun signOut() {
        auth.signOut()
        credentialManager.clearCredentialState(request = ClearCredentialStateRequest())
    }
}