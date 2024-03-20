package com.app.seoullo_new.view

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.net.toUri
import com.app.seoullo_new.R
import com.app.seoullo_new.base.BaseActivity
import com.app.seoullo_new.databinding.ActivityLoginBinding
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.utils.LoginState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels()

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun setup() {
        setBinding(R.layout.activity_login)
    }

    override fun onCreateView(savedInstanceState: Bundle?) {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // Initialize Firebase Auth
        auth = Firebase.auth

        viewModel.isLoginCheck()

        // Video
        binding.background.apply {
            setVideoURI(("android.resource://$packageName/raw/background").toUri())
            setOnPreparedListener { it.start() }
            setOnCompletionListener { it.start() }
        }

        observeFlow {
            viewModel.isLogin.collect {
                when (it) {
                    is LoginState.loading -> {}
                    is LoginState.IsUser -> {
                        if (it.state) {
                            moveMain()
                        }
                    }
                }
            }
        }
    }

    override fun onSingleClick(v: View) {
        when (v.id) {
            R.id.sign_in_with_google_ -> {
                // 회원 등록 이후 로그인
                signIn()
            }

            else -> super.onSingleClick(v)
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        signInForResult.launch(signInIntent)
    }

    private val signInForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)!!
                        Logging.e("firebaseAuthWithGoogle:" + account.id)
                        firebaseAuthWithGoogle(account.idToken!!)
                    } catch (e: ApiException) {
                        // Google Sign In failed, update UI appropriately
                        Logging.e("Google sign in failed", e)
                    }
                }
            }
        }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Logging.e("signInWithCredential:success")
//                    val user = auth.currentUser
                    auth.currentUser?.let { viewModel.onLoginSuccess(it) }
                } else {
                    // If sign in fails, display a message to the user.
                    Logging.e("signInWithCredential:failure" + task.exception?.message)
                }
            }
    }

    private fun moveMain() {

    }
}