package com.app.seoullo_new.utils

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.app.seoullo_new.R

class LoadingBar : LinearLayout {
    private val inAnimation: Animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
    private val outAnimation: Animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
    private val progressBar: ProgressBar = ProgressBar(context)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        progressBar.indeterminateDrawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, R.color.color_92c8e0), PorterDuff.Mode.MULTIPLY)
        addView(progressBar)
    }

    override fun setVisibility(visibility: Int) {
        when (visibility) {
            View.GONE, View.INVISIBLE -> hide(visibility)
            else -> show()
        }
    }

    override fun setSelected(selected: Boolean) {
        hide(selected)
        super.setSelected(selected)
    }

    fun show() {
        show(true)
    }

    fun show(withAnimation: Boolean) {
        if (withAnimation) startAnimation(inAnimation)
        super.setVisibility(View.VISIBLE)
    }

    private fun hide(visibility: Int) {
        if (!isVisible()) return
        hide(true, visibility)
    }

    private fun hide(withAnimation: Boolean, visibility: Int) {
        if (withAnimation) {
            outAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    super@LoadingBar.setVisibility(visibility)
                }

                override fun onAnimationStart(animation: Animation?) {}
            })
            startAnimation(outAnimation)
        } else {
            super.setVisibility(visibility)
        }
    }

    private fun hide(withAnimation: Boolean) {
        if (withAnimation) {
            outAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    super@LoadingBar.setVisibility(View.GONE)
                }

                override fun onAnimationStart(animation: Animation?) {}
            })
            startAnimation(outAnimation)
        } else {
            super.setVisibility(View.GONE)
        }
    }

    private fun isVisible(): Boolean {
        return visibility == View.VISIBLE
    }
}