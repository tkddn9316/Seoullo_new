package com.app.data.mapper

import com.app.data.model.PlacesDetailGoogleResponseDTO
import com.app.data.model.PlacesNearbyRequestDTO
import com.app.data.model.PlacesNearbyResponseDTO
import com.app.data.model.PlacesResponseDTO
import com.app.data.model.UserEntity
import com.app.data.model.WeatherDTO
import com.app.domain.model.Places
import com.app.domain.model.PlacesDetailGoogle
import com.app.domain.model.PlacesNearbyRequest
import com.app.domain.model.User
import com.app.domain.model.Weather

/**
 * Data Entity to Data Model
 * Data Layer 에서는 Data Entity 로 받아서 사용하지만, Domain, Presentation Layer 에서는 Data Model 로 사용한다.
 * 즉, Mapper 는 Data Layer 에 존재하면서 다른 계층으로 Data 를 전달할 때, 사용하는 Data Model 에 맞춰서 변환하여 던지는 역할.
 */

fun mapperToPlaceNearbyDTO(place: PlacesNearbyRequest): PlacesNearbyRequestDTO =
    PlacesNearbyRequestDTO(
        place.includedTypes,
        place.maxResultCount,
        place.languageCode,
        PlacesNearbyRequestDTO.LocationRestriction(
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
                it.name,
                it.id,
                it.displayName.text,
                it.formattedAddress,
                it.primaryTypeDisplayName?.text ?: "",
                it.regularOpeningHours?.openNow ?: run { false },
                it.regularOpeningHours?.weekdayDescriptions ?: run { emptyList() },
                it.rating,
                it.userRatingCount,
                it.photoUrl
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
        place.title,
        place.contentid,
        place.title,
        place.addr1,
        "",
        false,
        emptyList(),
        0.0,
        0,
        place.firstimage
    )

fun mapperToUser(userEntity: List<UserEntity>): List<User> =
    userEntity.toList().map {
        User(
            it.index,
            it.auto,
            it.name,
            it.email,
            it.photoUrl
        )
    }

fun mapperToWeather(weatherDTO: WeatherDTO): List<Weather> =
    weatherDTO.response.body.items.item.toList().map {
        Weather(
            it.baseData,
            it.baseTime,
            it.category,
            it.fcstDate,
            it.fcstTime,
            it.fcstValue,
            it.nx,
            it.ny
        )
    }