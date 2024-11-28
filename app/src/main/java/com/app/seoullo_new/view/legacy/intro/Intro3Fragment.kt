package com.app.seoullo_new.view.legacy.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.app.seoullo_new.R
import com.app.seoullo_new.view.base.BaseFragment
import com.app.seoullo_new.databinding.FragmentIntro3Binding
import com.app.seoullo_new.utils.PreferenceManager
import com.app.seoullo_new.utils.Util.launchActivity
import com.app.seoullo_new.view.main.MainActivity

class Intro3Fragment : BaseFragment<FragmentIntro3Binding, IntroViewModel>() {
    override val viewModel: IntroViewModel by viewModels()
    private val preferences by lazy { PreferenceManager(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return setBinding(
            inflater, container, R.layout.fragment_intro3
        )
    }

    override fun onSingleClick(v: View) {
        when(v.id) {
            R.id.start_ -> {
                launchActivity<MainActivity>()
                requireActivity().finish()
                preferences.setIsIntro(false)
            }
            else -> super.onSingleClick(v)
        }
    }
}