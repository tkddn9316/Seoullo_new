package com.app.seoullo_new.utils

import android.view.View

interface OnSingleClickListener : View.OnClickListener {
    fun onSingleClick(v: View)
    fun onItemClick(v: View)

    companion object {
        const val CLICK_INTERVAL = 300L
    }
}