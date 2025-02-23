package com.app.data.utils

import android.util.Xml
import com.app.data.model.Body
import com.app.data.model.Header
import com.app.data.model.Item
import com.app.data.model.Items
import com.app.data.model.SunriseDTO
import okhttp3.ResponseBody
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.StringReader

class SunriseXmlParser {
    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(responseBody: ResponseBody): SunriseDTO {
        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(StringReader(responseBody.string()))
        parser.nextTag()
        return readResponse(parser)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readResponse(parser: XmlPullParser): SunriseDTO {
        var header: Header? = null
        var body: Body? = null
        var numOfRows = 0
        var pageNo = 0
        var totalCount = 0

        parser.require(XmlPullParser.START_TAG, null, "response")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            when (parser.name) {
                "header" -> header = readHeader(parser)
                "body" -> body = readBody(parser)
                "numOfRows" -> numOfRows = readInt(parser, "numOfRows")
                "pageNo" -> pageNo = readInt(parser, "pageNo")
                "totalCount" -> totalCount = readInt(parser, "totalCount")
                else -> skip(parser)
            }
        }
        return SunriseDTO(header, body, numOfRows, pageNo, totalCount)
    }

    private fun readHeader(parser: XmlPullParser): Header {
        var resultCode = ""
        var resultMsg = ""
        parser.require(XmlPullParser.START_TAG, null, "header")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            when (parser.name) {
                "resultCode" -> resultCode = readText(parser)
                "resultMsg" -> resultMsg = readText(parser)
                else -> skip(parser)
            }
        }
        return Header(resultCode, resultMsg)
    }

    private fun readBody(parser: XmlPullParser): Body {
        var items: Items? = null
        parser.require(XmlPullParser.START_TAG, null, "body")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            when (parser.name) {
                "items" -> items = readItems(parser)
                else -> skip(parser)
            }
        }
        return Body(items)
    }

    private fun readItems(parser: XmlPullParser): Items {
        val itemList = mutableListOf<Item>()
        parser.require(XmlPullParser.START_TAG, null, "items")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            if (parser.name == "item") itemList.add(readItem(parser))
            else skip(parser)
        }
        return Items(itemList)
    }

    private fun readItem(parser: XmlPullParser): Item {
        val map = mutableMapOf<String, String>()
        parser.require(XmlPullParser.START_TAG, null, "item")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            val name = parser.name
            map[name] = readText(parser)
        }
        return Item(
            aste = map["aste"] ?: "",
            astm = map["astm"] ?: "",
            civile = map["civile"] ?: "",
            civilm = map["civilm"] ?: "",
            latitude = map["latitude"] ?: "",
            latitudeNum = map["latitudeNum"] ?: "",
            location = map["location"] ?: "",
            locdate = map["locdate"] ?: "",
            longitude = map["longitude"] ?: "",
            longitudeNum = map["longitudeNum"] ?: "",
            moonrise = map["moonrise"] ?: "",
            moonset = map["moonset"] ?: "",
            moontransit = map["moontransit"] ?: "",
            naute = map["naute"] ?: "",
            nautm = map["nautm"] ?: "",
            sunrise = map["sunrise"] ?: "",
            sunset = map["sunset"] ?: "",
            suntransit = map["suntransit"] ?: ""
        )
    }

    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun readInt(parser: XmlPullParser, tag: String): Int {
        return readText(parser).toIntOrNull() ?: 0
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) return
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}