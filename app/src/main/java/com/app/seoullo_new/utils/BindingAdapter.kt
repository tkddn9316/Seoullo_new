package com.app.seoullo_new.utils

import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

object BindingAdapter {
    private fun Boolean?.gone(): Int {
        return when {
            this == true -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun Boolean?.invisible(): Int {
        return when {
            this == true -> View.VISIBLE
            else -> View.INVISIBLE
        }
    }

    @JvmStatic
    @BindingAdapter("enable")
    fun enable(v: View, value: Boolean) {
        v.isEnabled = value
    }

    @JvmStatic
    @BindingAdapter("gone")
    fun View.isGone(visibility: Boolean?) {
        this.visibility = visibility.gone()
    }

    @JvmStatic
    @BindingAdapter("invisible")
    fun View.isInvisible(visibility: Boolean?) {
        this.visibility = visibility.invisible()
    }

    @JvmStatic
    @BindingAdapter("adapter")
    fun bindAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        recyclerView.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("text")
    fun bindText(v: TextView, text: String?) {
        if (text.isNullOrEmpty()) return
        if (text == v.text.toString()) return
        v.text = toHtml(text)
    }

    private fun toHtml(text: String): Spanned {
        val value = text.replace("&amp;", "&")
            .replace("\n", "<br>")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&apos;", "'")
            .replace("&quot;", "\"")
            .replace("&nbsp;", " ")

        return Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY)
    }
}