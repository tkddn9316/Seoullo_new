package com.app.data.mapper

import android.text.Html
import com.app.data.model.PlacesDetailGoogleResponseDTO
import com.app.data.model.PlacesDetailResponseDTO
import com.app.data.model.PlacesNearbyRequestDTO
import com.app.data.model.PlacesNearbyResponseDTO
import com.app.data.model.PlacesResponseDTO
import com.app.data.model.ReverseGeocodingDTO
import com.app.data.model.UserEntity
import com.app.data.model.WeatherDTO
import com.app.data.utils.Util.ApiSuccessCode
import com.app.data.utils.Util.addHttps
import com.app.domain.model.Places
import com.app.domain.model.PlacesDetail
import com.app.domain.model.PlacesDetailGoogle
import com.app.domain.model.PlacesNearbyRequest
import com.app.domain.model.ReverseGeocoding
import com.app.domain.model.User
import com.app.domain.model.Weather

/**
 * Data Entity to Data Model
 * Data Layer 에서는 Data Entity 로 받아서 사용하지만, Domain, Presentation Layer 에서는 Data Model 로 사용한다.
 * 즉, Mapper 는 Data Layer 에 존재하면서 다른 계층으로 Data 를 전달할 때, 사용하는 Data Model 에 맞춰서 변환하여 던지는 역할.
 */

fun mapperToPlaceNearbyDTO(place: PlacesNearbyRequest): PlacesNearbyRequestDTO =
    PlacesNearbyRequestDTO(
        includedTypes = place.includedTypes,
        maxResultCount = place.maxResultCount,
        languageCode = place.languageCode,
        locationRestriction = PlacesNearbyRequestDTO.LocationRestriction(
            circle = PlacesNearbyRequestDTO.Circle(
                center = PlacesNearbyRequestDTO.Center(
                    place.locationRestriction.circle.center.latitude,
                    place.locationRestriction.circle.center.longitude
                ),
                radius = place.locationRestriction.circle.radius
            )
        )
    )

fun mapperToPlaceNearby(place: PlacesNearbyResponseDTO): List<Places> =
    place.place?.let { list ->
        list.toList().map {
            Places(
                name = it.name,
                id = it.id,
                contentTypeId = "",
                displayName = it.displayName.text,
                address = it.formattedAddress,
                description = it.primaryTypeDisplayName?.text ?: "",
                openNow = it.regularOpeningHours?.openNow ?: run { false },
                weekdayDescriptions = it.regularOpeningHours?.weekdayDescriptions
                    ?: run { emptyList() },
                rating = it.rating,
                userRatingCount = it.userRatingCount,
                photoUrl = it.photoUrl
            )
        }
    } ?: run { emptyList() }

fun mapperToPlaceDetailGoogle(place: PlacesDetailGoogleResponseDTO): PlacesDetailGoogle =
    PlacesDetailGoogle(
        latitude = place.location.latitude,
        longitude = place.location.longitude,
        reviews = place.reviews?.let { list ->
            list.toList().map {
                PlacesDetailGoogle.Review(
                    profileName = it.authorAttribution.profileName,
                    profilePhotoUrl = it.authorAttribution.profilePhotoUrl,
                    relativePublishTimeDescription = it.relativePublishTimeDescription,
                    rating = it.rating,
                    text = it.text?.text ?: ""  // 텍스트 없는 리뷰
                )
            }
        } ?: run { emptyList() },   // 리뷰가 아예 없을 경우
        reviewsUri = place.googleMapsLinks.reviewsUri,
        phoneNumber = place.nationalPhoneNumber ?: ""
    )

fun mapperToPlace(place: PlacesResponseDTO.Place): Places =
    Places(
        name = place.title,
        id = place.contentid,
        contentTypeId = place.contenttypeid,
        displayName = place.title,
        address = place.addr1,
        description = "",
        openNow = false,
        weekdayDescriptions = emptyList(),
        rating = 0.0,
        userRatingCount = 0,
        photoUrl = place.firstimage
    )

fun mapperToPlaceDetail(placesDetail: PlacesDetailResponseDTO.PlacesDetail): PlacesDetail =
    PlacesDetail(
        contentId = placesDetail.contentid,
        contentTypeId = placesDetail.contenttypeid,
        displayName = placesDetail.title,
        address = placesDetail.addr1,
        description = Html.fromHtml(placesDetail.overview, Html.FROM_HTML_MODE_LEGACY).toString(),
        photoUrl = placesDetail.firstimage,
        latitude = placesDetail.mapy.toDouble(),
        longitude = placesDetail.mapx.toDouble(),
        phoneNum = placesDetail.tel,
        homepage = if (placesDetail.homepage.isNotEmpty()) Html.fromHtml(
            placesDetail.homepage,
            Html.FROM_HTML_MODE_LEGACY
        ).toString().addHttps() else ""
    )

fun mapperToReverseGeocoding(reverseGeocodingDTO: ReverseGeocodingDTO): ReverseGeocoding {
    val firstResult = reverseGeocodingDTO.results?.firstOrNull()
    return ReverseGeocoding(
        address = firstResult?.address.orEmpty(),
        placeId = firstResult?.placeId.orEmpty()
    )
}

fun mapperToUser(userEntity: List<UserEntity>): List<User> =
    userEntity.toList().map {
        User(
            index = it.index,
            auto = it.auto,
            name = it.name,
            email = it.email,
            photoUrl = it.photoUrl
        )
    }

fun mapperToWeather(weatherDTO: WeatherDTO): List<Weather> {
    val header = weatherDTO.response.header
    if (header.resultCode != ApiSuccessCode.Weather.code) {
        throw Exception("${header.resultCode}: ${header.resultMsg}")
    }

    return weatherDTO.response.body?.items?.let { items ->
        items.item.toList().map {
            Weather(
                baseData = it.baseData,
                baseTime = it.baseTime,
                category = it.category,
                fcstDate = it.fcstDate,
                fcstTime = it.fcstTime,
                fcstValue = it.fcstValue,
                nx = it.nx,
                ny = it.ny
            )
        }
    } ?: run { emptyList() }
}