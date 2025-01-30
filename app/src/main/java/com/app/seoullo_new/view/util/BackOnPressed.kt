package com.app.seoullo_new.view.util

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.app.seoullo_new.R

@Composable
fun BackOnPressed() {
    val context = LocalContext.current
    var backPressedTime = 0L

    BackHandler(enabled = true) {
        if(System.currentTimeMillis() - backPressedTime <= 400L) {
            (context as Activity).finish() // 앱 종료
        } else {
            Toast.makeText(context, context.getString(R.string.exit), Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}