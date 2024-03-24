package com.app.seoullo_new.view

import android.os.Bundle
import androidx.activity.viewModels
import com.app.seoullo_new.R
import com.app.seoullo_new.base.BaseActivity
import com.app.seoullo_new.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

// 탭 3~4개
// 탭1: 메인 배너, popular location
// 탭2: travel info
// 탭3: 설정 창(로그아웃)
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel2>() {
    override val viewModel: MainViewModel2 by viewModels()

    override fun setup() {
        setBinding(R.layout.activity_main)
    }

    override fun onCreateView(savedInstanceState: Bundle?) {

    }
}