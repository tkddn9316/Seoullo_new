package com.app.seoullo_new.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.ViewDataBinding
import com.app.seoullo_new.utils.OnSingleClickListener

abstract class BaseActivity<VDB : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity(),
    OnSingleClickListener {
    protected lateinit var viewModel: VM
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
//        FLog.e(TAG, "onCreate")
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