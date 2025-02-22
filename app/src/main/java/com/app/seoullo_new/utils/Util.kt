package com.app.seoullo_new.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.domain.model.common.BaseModel
import com.app.domain.model.theme.Language
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Constants.INTENT_DATA
import com.app.seoullo_new.view.util.TravelItemData
import com.app.seoullo_new.view.util.TravelJsonItemData
import com.google.android.gms.maps.model.LatLng
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

    // 관광명소 request data
    fun getAttraction(travelData: TravelItemData): List<TravelJsonItemData> {
        return travelData.attraction ?: emptyList()
    }

    // 쇼핑 request data
    fun getShopping(travelData: TravelItemData): List<TravelJsonItemData> {
        return travelData.shopping ?: emptyList()
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
            "Nature" -> R.string.nature
            "Historical" -> R.string.historical
            "Culture" -> R.string.culture
            "Leisure Sports" -> R.string.leisure_sports
            "Department Store" -> R.string.department_store
            "Market" -> R.string.market
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

    /** 구글 지도 Polyline decoding */
    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = mutableListOf<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val point = LatLng(lat / 1E5, lng / 1E5)
            poly.add(point)
        }

        return poly
    }

    fun String.toColor(): Color {
        return Color(android.graphics.Color.parseColor(this))
    }

    fun getLanguageCode(context: Context, language: Language): String {
        return if (language == Language.ENGLISH) context.getString(R.string.en) else context.getString(R.string.ko)
    }
}