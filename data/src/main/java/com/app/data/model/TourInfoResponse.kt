package com.app.data.model

import com.google.gson.annotations.SerializedName

data class TourInfoResponse(
    @SerializedName("response")
    val response: Response
) {
    data class Response(
        @SerializedName("body")
        val body: Body
    )

    data class Body(
        @SerializedName("items")
        val items: Items,
        @SerializedName("numOfRows")
        val numOfRows: Int,
        @SerializedName("pageNo")
        val pageNo: Int,
        @SerializedName("totalCount")
        val totalCount: Int
    )

    data class Items(
        @SerializedName("item")
        val items: List<TourInfoDTO>
    )
}
