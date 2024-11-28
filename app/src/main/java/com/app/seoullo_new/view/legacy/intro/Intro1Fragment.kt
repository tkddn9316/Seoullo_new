package com.app.seoullo_new.view.legacy.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.app.seoullo_new.R
import com.app.seoullo_new.view.base.BaseFragment
import com.app.seoullo_new.databinding.FragmentIntro1Binding
import com.app.seoullo_new.utils.PreferenceManager
import com.app.seoullo_new.utils.Util.launchActivity
import com.app.seoullo_new.view.main.MainActivity

class Intro1Fragment : BaseFragment<FragmentIntro1Binding, IntroViewModel>() {
    override val viewModel: IntroViewModel by viewModels()
    private val preferences by lazy { PreferenceManager(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return setBinding(
            inflater, container, R.layout.fragment_intro1
        )
    }

    override fun onSingleClick(v: View) {
        when (v.id) {
            R.id.skip_ -> {
                launchActivity<MainActivity>()
                requireActivity().finish()
                preferences.setIsIntro(false)
            }

            R.id.next_ -> {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.intro1Fragment, true)
                    .setEnterAnim(R.anim.right_in)
                    .setExitAnim(R.anim.right_out)
                    .build()
                Navigation.findNavController(v)
                    .navigate(R.id.action_intro1Fragment_to_intro2Fragment, null, navOptions)
            }

            else -> super.onSingleClick(v)
        }
    }
}