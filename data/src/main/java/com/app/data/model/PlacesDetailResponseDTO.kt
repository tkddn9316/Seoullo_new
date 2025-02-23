package com.app.data.model

import com.google.gson.annotations.SerializedName

data class PlacesDetailResponseDTO(
    @SerializedName("response")
    val response: Response
) {
    data class Response(
        @SerializedName("body")
        val body: Body
    )

    data class Body(
        @SerializedName("items")
        val items: Items?,
        @SerializedName("numOfRows")
        val numOfRows: Int,
        @SerializedName("pageNo")
        val pageNo: Int,
        @SerializedName("totalCount")
        val totalCount: Int
    )

    data class Items(
        @SerializedName("item")
        val items: List<PlacesDetail>
    )

    data class PlacesDetail(
        @SerializedName("addr1")
        val addr1: String?,
        @SerializedName("addr2")
        val addr2: String?,
        @SerializedName("contentid")
        val contentid: String?,
        @SerializedName("contenttypeid")
        val contenttypeid: String?,
        @SerializedName("cpyrhtDivCd")
        val cpyrhtDivCd: String?,
        @SerializedName("createdtime")
        val createdtime: String?,
        @SerializedName("firstimage")
        val firstimage: String?,
        @SerializedName("firstimage2")
        val firstimage2: String?,
        @SerializedName("homepage")
        val homepage: String?,
        @SerializedName("mapx")
        val mapx: String?,
        @SerializedName("mapy")
        val mapy: String?,
        @SerializedName("mlevel")
        val mlevel: String?,
        @SerializedName("modifiedtime")
        val modifiedtime: String?,
        @SerializedName("overview")
        val overview: String?,
        @SerializedName("tel")
        val tel: String?,
        @SerializedName("telname")
        val telname: String?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("zipcode")
        val zipcode: String?
    )
}
