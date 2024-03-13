package com.app.seoullo_new.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.seoullo_new.R
import com.app.seoullo_new.base.BaseActivity
import com.app.seoullo_new.databinding.ActivityLoginBinding
import com.app.seoullo_new.utils.Logging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun setup() {
        setBinding(R.layout.activity_login)
    }

    override fun onCreateView(savedInstanceState: Bundle?) {
        // Video
        binding.background.apply {
            setVideoURI(("android.resource://$packageName/raw/background").toUri())
            setOnPreparedListener { it.start() }
            setOnCompletionListener { it.start() }
        }

        // Button Visible
//        observeFlow {  }
    }
}