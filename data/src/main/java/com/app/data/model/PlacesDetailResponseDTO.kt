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
        val addr1: String,
        val addr2: String,
        val contentid: String,
        val contenttypeid: String,
        val cpyrhtDivCd: String,
        val createdtime: String,
        val firstimage: String,
        val firstimage2: String,
        val homepage: String,
        val mapx: String,
        val mapy: String,
        val mlevel: String,
        val modifiedtime: String,
        val overview: String,
        val tel: String,
        val telname: String,
        val title: String,
        val zipcode: String
    )
}
