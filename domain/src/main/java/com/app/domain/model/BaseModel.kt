package com.app.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.app.domain.util.DateTimeTypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.joda.time.DateTime

open class BaseModel : Parcelable {
    constructor()

    constructor(source: Parcel) : this()

    open fun isSuccess(): Boolean {
        return false
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return getGson().toJson(this) + this::class.java.simpleName
    }

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {

    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<BaseModel> = object : Parcelable.Creator<BaseModel> {
            override fun createFromParcel(source: Parcel): BaseModel = BaseModel(source)
            override fun newArray(size: Int): Array<BaseModel?> = arrayOfNulls(size)
        }
    }

    fun getGson(): Gson {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(DateTime::class.java, DateTimeTypeConverter())
        //        builder.registerTypeAdapter(Boolean.class, new BooleanTypeConverter());
        return builder.create()
    }
}