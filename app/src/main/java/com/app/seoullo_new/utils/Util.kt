package com.app.seoullo_new.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.domain.model.common.BaseModel
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Constants.INTENT_DATA
import com.app.seoullo_new.view.util.TravelItemData
import com.app.seoullo_new.view.util.TravelJsonItemData
import kotlinx.serialization.json.Json

/**
 * 각종 유틸 클래스
 */
object Util {
    // Intent(for Compose)
    inline fun <reified T : Activity> Context.launchActivity(
        model: BaseModel? = null,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}
    ) {
        launchActivity<T>(-1, model, options, init)
    }

    inline fun <reified T : Activity> Context.launchActivity(
        requestCode: Int = -1,
        model: BaseModel? = null,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}
    ) {
        val intent = Intent(this, T::class.java).apply {
            model?.let { putExtra(INTENT_DATA, it) }
            init()
        }
        if (this is Activity && requestCode > 0) {
            startActivityForResult(intent, requestCode, options)
        } else {
            startActivity(intent, options)
        }
    }

    // Activity intent
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

    // Fragment intent
    inline fun <reified T : Activity> Fragment.launchActivity(
        model: BaseModel? = null,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}
    ) {

        val intent = requireContext().newIntent<T>()
            .apply { model?.let { putExtra(INTENT_DATA, it) } }
        intent.init()
        startActivity(intent, options)
    }

    inline fun <reified T : Activity> Fragment.launchActivity(
        data: String,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}
    ) {

        val intent = requireContext().newIntent<T>().apply { putExtra(INTENT_DATA, data) }
        intent.init()
        startActivity(intent, options)

    }

    fun Class<*>.getTag(): String {
        return this.simpleName
    }

    fun loadTravelData(jsonString: String): TravelItemData {
        return Json.decodeFromString(jsonString)
    }

    fun loadDrawableResource(context: Context, iconName: String): Int {
        return context.resources.getIdentifier(iconName, "drawable", context.packageName)
    }

    fun loadJsonFromAssets(context: Context): String {
        return context.assets.open("content_data.json").bufferedReader().use { it.readText() }
    }

    // 음식점 request data
    fun getRestaurants(travelData: TravelItemData): List<TravelJsonItemData> {
        return travelData.restaurant ?: emptyList()
    }

    // 숙박시설 request data
    fun getAccommodations(travelData: TravelItemData): List<TravelJsonItemData> {
        return travelData.accommodation ?: emptyList()
    }

    fun getStringResourceKey(name: String): Int {
        return when (name) {
            "KoreanRestaurant" -> R.string.korean_restaurant
            "JapaneseRestaurant" -> R.string.japanese_restaurant
            "ChineseRestaurant" -> R.string.chinese_restaurant
            "WesternRestaurant" -> R.string.western_restaurant
            "Bar, Cafe" -> R.string.bar_cafe
            "Hotel" -> R.string.hotel
            "Motel" -> R.string.motel
            "Guest House" -> R.string.guest_house
            "Condominium" -> R.string.condominium
            else -> R.string.empty
        }
    }

    fun Context.hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}