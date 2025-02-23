package com.app.data.model

data class SunriseDTO(
    val header: Header?,
    val body: Body?,
    val numOfRows: Int = 0,
    val pageNo: Int = 0,
    val totalCount: Int = 0
)

data class Header(
    val resultCode: String,
    val resultMsg: String
)

data class Body(
    val items: Items?
)

data class Items(
    val itemList: List<Item>?
)

data class Item(
    val aste: String = "",
    val astm: String = "",
    val civile: String = "",
    val civilm: String = "",
    val latitude: String = "",
    val latitudeNum: String = "",
    val location: String = "",
    val locdate: String = "",
    val longitude: String = "",
    val longitudeNum: String = "",
    val moonrise: String = "",
    val moonset: String = "",
    val moontransit: String = "",
    val naute: String = "",
    val nautm: String = "",
    val sunrise: String = "",
    val sunset: String = "",
    val suntransit: String = ""
)


//@Root(name = "response", strict = false)
//data class SunriseDTO(
//    @field:Element(name = "header")
//    var header: Header? = null,
//
//    @field:Element(name = "body")
//    var body: Body? = null,
//
//    @field:Element(name = "numOfRows", required = false)
//    var numOfRows: Int = 0,
//
//    @field:Element(name = "pageNo", required = false)
//    var pageNo: Int = 0,
//
//    @field:Element(name = "totalCount", required = false)
//    var totalCount: Int = 0
//)
//
//@Root(name = "header", strict = false)
//data class Header(
//    @field:Element(name = "resultCode", required = false)
//    var resultCode: String = "",
//
//    @field:Element(name = "resultMsg", required = false)
//    var resultMsg: String = ""
//)
//
//@Root(name = "body", strict = false)
//data class Body(
//    @field:Element(name = "items", required = false)
//    var items: Items? = null
//)
//
//@Root(name = "items", strict = false)
//data class Items(
//    @field:ElementList(name = "item", inline = true, required = false)
//    var itemList: List<Item>? = null
//)
//
//@Root(name = "item", strict = false)
//data class Item(
//    @field:Element(name = "aste", required = false)
//    var aste: String = "",
//
//    @field:Element(name = "astm", required = false)
//    var astm: String = "",
//
//    @field:Element(name = "civile", required = false)
//    var civile: String = "",
//
//    @field:Element(name = "civilm", required = false)
//    var civilm: String = "",
//
//    @field:Element(name = "latitude", required = false)
//    var latitude: String = "",
//
//    @field:Element(name = "latitudeNum", required = false)
//    var latitudeNum: String = "",
//
//    @field:Element(name = "location", required = false)
//    var location: String = "",
//
//    @field:Element(name = "locdate", required = false)
//    var locdate: String = "",
//
//    @field:Element(name = "longitude", required = false)
//    var longitude: String = "",
//
//    @field:Element(name = "longitudeNum", required = false)
//    var longitudeNum: String = "",
//
//    @field:Element(name = "moonrise", required = false)
//    var moonrise: String = "",
//
//    @field:Element(name = "moonset", required = false)
//    var moonset: String = "",
//
//    @field:Element(name = "moontransit", required = false)
//    var moontransit: String = "",
//
//    @field:Element(name = "naute", required = false)
//    var naute: String = "",
//
//    @field:Element(name = "nautm", required = false)
//    var nautm: String = "",
//
//    @field:Element(name = "sunrise", required = false)
//    var sunrise: String = "",
//
//    @field:Element(name = "sunset", required = false)
//    var sunset: String = "",
//
//    @field:Element(name = "suntransit", required = false)
//    var suntransit: String = ""
//)
