package com.app.seoullo_new.utils

import android.content.Context
import android.content.SharedPreferences
import com.app.seoullo_new.utils.PreferenceHelper.get
import com.app.seoullo_new.utils.PreferenceHelper.set

class PreferenceManager(context: Context) {
    private val prefs: SharedPreferences = PreferenceHelper.defaultPrefs(context)

    fun setIsIntro(value: Boolean) {
        prefs["intro"] = value
    }

    fun getIsIntro(): Boolean {
        return prefs["intro", true]
    }
}