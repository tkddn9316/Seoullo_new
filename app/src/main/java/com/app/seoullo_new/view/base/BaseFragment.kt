package com.app.seoullo_new.view.base

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.utils.OnSingleClickListener
import com.app.seoullo_new.utils.Util
import com.app.seoullo_new.utils.Util.getTag
import com.app.seoullo_new.BR
import com.google.firebase.installations.Utils

abstract class BaseFragment<VDB : ViewDataBinding, VM : BaseViewModel> : Fragment(), OnSingleClickListener {
    protected var TAG: String = BaseFragment::class.java.simpleName
    private var lastClickTime = 0L
    private lateinit var context: Context
    abstract val viewModel: VM
    protected lateinit var binding: VDB

    private fun setContext(context: Context) {
        this.context = context
    }

    protected fun setContentView(inflater: LayoutInflater, container: ViewGroup, res: Int): View {
        return inflater.inflate(res, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = Util.javaClass.getTag()
        setContext(requireActivity())
        Logging.e(TAG, "onCreate")
    }

    protected open fun setBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        @LayoutRes layoutId: Int
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutId, parent, false)
        binding.setVariable(BR.view, this)
        binding.setVariable(BR.viewModel, viewModel)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onClick(v: View) {
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - lastClickTime
        lastClickTime = currentClickTime
        // duplicate
        if (elapsedTime <= OnSingleClickListener.CLICK_INTERVAL) return

        onSingleClick(v)
    }

    override fun onSingleClick(v: View) {

    }

    override fun onItemClick(v: View) {

    }
}