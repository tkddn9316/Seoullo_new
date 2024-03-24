package com.app.seoullo_new.view

import android.os.Bundle
import androidx.activity.viewModels
import com.app.seoullo_new.R
import com.app.seoullo_new.base.BaseActivity
import com.app.seoullo_new.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

// 탭 3~4개(navigation으로 구성, ViewPager X)
// 탭1: 메인 배너, popular location
// 탭2: travel info
// 탭3: 설정 창(로그아웃)
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override val viewModel: MainViewModel by viewModels()

    override fun setup() {
        setBinding(R.layout.activity_main)
    }

    override fun onCreateView(savedInstanceState: Bundle?) {

    }
}