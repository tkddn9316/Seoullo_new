package com.app.seoullo_new.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.app.domain.model.BaseModel
import com.app.seoullo_new.utils.Constants.INTENT_DATA

/**
 * 각종 유틸 클래스
 */
object Util {

    inline fun <reified T : Activity> Activity.launchActivity(
        model: BaseModel? = null,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}
    ) {
        launchActivity<T>(-1, model, options, init)
    }

    inline fun <reified T : Activity> Activity.launchActivity(
        requestCode: Int = -1,
        model: BaseModel? = null,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}
    ) {
        val intent = newIntent<T>().apply { model?.let { putExtra(INTENT_DATA, it) } }
        intent.init()
        if (requestCode > 0) {
            startActivityForResult(intent, requestCode, options)
        } else {
            startActivity(intent, options)
        }
    }

    inline fun <reified T : Activity> Activity.launchActivity(
        data: String,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}
    ) {
        launchActivity<T>(-1, data, options, init)
    }

    inline fun <reified T : Activity> Activity.launchActivity(
        requestCode: Int = -1,
        text: String,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}
    ) {

        val intent = newIntent<T>().apply { putExtra(INTENT_DATA, text) }
        intent.init()
        if (requestCode > 0) {
            startActivityForResult(intent, requestCode, options)
        } else {
            startActivity(intent, options)
        }
    }

    inline fun <reified T : Activity> Context.newIntent(): Intent = Intent(this, T::class.java)

    inline fun <reified T : Activity> Context.newIntent(data: String): Intent =
        Intent(this, T::class.java).apply { putExtra(INTENT_DATA, data) }
}