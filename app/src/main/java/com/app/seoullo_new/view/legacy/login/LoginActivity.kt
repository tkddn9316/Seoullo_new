package com.app.seoullo_new.view.legacy.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.net.toUri
import com.app.seoullo_new.R
import com.app.seoullo_new.view.base.BaseActivity
import com.app.seoullo_new.databinding.ActivityLoginBinding
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.utils.LoginState
import com.app.seoullo_new.utils.PreferenceManager
import com.app.seoullo_new.utils.Util.launchActivity
import com.app.seoullo_new.view.legacy.intro.IntroActivity
import com.app.seoullo_new.view.main.MainActivity
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
        // TODO: 구글 로그인 변경 예정: https://soopeach.tistory.com/176
        //  https://inblog.ai/code-with-me/기존-google-로그인-대신-credential-manager-api를-활용한-sign-in-with-google-로그인-구현하기-21777
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

                else -> {
                    Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
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
        val preferences = PreferenceManager(this)
        if (preferences.getIsIntro()) {
            launchActivity<IntroActivity>()
        } else {
//            launchActivity<PlacesListActivity>()
            launchActivity<MainActivity>()
        }
        finish()
//        if (Build.VERSION.SDK_INT >= 34) {
//            overrideActivityTransition(
//                Activity.OVERRIDE_TRANSITION_OPEN, R.anim.abc_fade_in, R.anim.abc_fade_out, Color.TRANSPARENT
//            )
//        } else {
//            @Suppress("DEPRECATION")
//            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
//        }
        @Suppress("DEPRECATION")
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
    }
}