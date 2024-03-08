package com.app.seoullo_new.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.app.seoullo_new.BR
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.utils.OnSingleClickListener

abstract class BaseActivity<VDB : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity(),
    OnSingleClickListener {
    abstract val viewModel: VM
    protected lateinit var binding: VDB
    protected val TAG: String by lazy {
        javaClass.simpleName
    }
    protected lateinit var context: Context
    protected lateinit var activity: BaseActivity<VDB, VM>
    private var lastClickTime = 0L

    abstract fun setup()

    abstract fun onCreateView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Logging.e(TAG, "onCreate")
        context = this
        activity = this
        setup()
        onCreateView(savedInstanceState)
    }

    override fun onSingleClick(v: View) {
//        if (v.id == R.id.back_) finish()
    }

    override fun onItemClick(v: View) {

    }

    open fun setBinding(@LayoutRes layoutId: Int) {
        binding = DataBindingUtil.setContentView<VDB>(this, layoutId).apply {
            setVariable(BR.viewModel, viewModel)
            setVariable(BR.view, this@BaseActivity)
            lifecycleOwner = this@BaseActivity
        }
    }

    override fun onClick(v: View) {
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - lastClickTime
        lastClickTime = currentClickTime
        // duplicate
        if (elapsedTime <= OnSingleClickListener.CLICK_INTERVAL) {
            return
        }
        onSingleClick(v)
    }
}