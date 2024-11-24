package com.app.seoullo_new.view.old

import android.os.Bundle
import androidx.activity.viewModels
import com.app.seoullo_new.R
import com.app.seoullo_new.base.BaseActivity
import com.app.seoullo_new.databinding.ActivityPlacesListBinding
import com.app.seoullo_new.view.placeList.PlacesListViewModel
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlacesListActivity : BaseActivity<ActivityPlacesListBinding, PlacesListViewModel>() {

    override val viewModel: PlacesListViewModel by viewModels()

    override fun setup() {
        setBinding(R.layout.activity_places_list)
    }

    override fun onCreateView(savedInstanceState: Bundle?) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        viewModel.checkPermission(fusedLocationProviderClient)
//        viewModel.getTourInfo()

        observeFlow {
            viewModel.placesListResult.collect {
                binding.text.text = it.toString()
            }
        }
    }
}