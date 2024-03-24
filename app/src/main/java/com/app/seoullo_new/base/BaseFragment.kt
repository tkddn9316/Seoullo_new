package com.app.seoullo_new.base

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.app.seoullo_new.utils.OnSingleClickListener

abstract class BaseFragment<VDB : ViewDataBinding, VM : BaseViewModel> : Fragment(), OnSingleClickListener {

}