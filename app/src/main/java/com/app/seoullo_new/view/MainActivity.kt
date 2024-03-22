package com.app.seoullo_new.view

import android.os.Bundle
import androidx.activity.viewModels
import com.app.seoullo_new.R
import com.app.seoullo_new.base.BaseActivity
import com.app.seoullo_new.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override val viewModel: MainViewModel by viewModels()

    override fun setup() {
        setBinding(R.layout.activity_main)
    }

    override fun onCreateView(savedInstanceState: Bundle?) {

    }
}