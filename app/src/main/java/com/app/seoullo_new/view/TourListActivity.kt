package com.app.seoullo_new.view

import android.os.Bundle
import androidx.activity.viewModels
import com.app.seoullo_new.R
import com.app.seoullo_new.base.BaseActivity
import com.app.seoullo_new.databinding.ActivityTourListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TourListActivity : BaseActivity<ActivityTourListBinding, TourListViewModel>() {

    override val viewModel: TourListViewModel by viewModels()

    override fun setup() {
        setBinding(R.layout.activity_tour_list)
    }

    override fun onCreateView(savedInstanceState: Bundle?) {
        viewModel.getTourInfo()

        observeFlow {
            viewModel.tourInfoListResult.collect {
                binding.text.text = it.toString()
            }
        }
    }
}