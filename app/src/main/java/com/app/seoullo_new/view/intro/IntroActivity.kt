package com.app.seoullo_new.view.intro

import android.os.Bundle
import androidx.activity.viewModels
import com.app.seoullo_new.R
import com.app.seoullo_new.base.BaseActivity
import com.app.seoullo_new.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity<ActivityIntroBinding, IntroViewModel>() {
    // TODO: 디자인 참고: https://pixso.net/app/editor/Vli_O-7XlS0U3PZ0_R3HSQ?page-id=0%3A1&item-id=1%3A3728
    override val viewModel: IntroViewModel by viewModels()

    override fun setup() {
        setBinding(R.layout.activity_intro)
    }

    override fun onCreateView(savedInstanceState: Bundle?) {

    }
}