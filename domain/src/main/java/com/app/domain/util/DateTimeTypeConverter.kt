package com.app.domain.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.lang.reflect.Type

class DateTimeTypeConverter : JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
    val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val DATE_FORMAT2 = "yyyy-MM-dd HH:mm:ss"

    override fun serialize(
        src: DateTime,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.millis / 1000)
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): DateTime {
        return try {
            return DateTime(json.asLong * 1000)
        } catch (e: Exception) {
            val jsonString = json.asString
            DateTimeFormat.forPattern(if (jsonString.lastIndexOf("Z") > 0) DATE_FORMAT else DATE_FORMAT2)
                .parseDateTime(jsonString)
        }
    }
}