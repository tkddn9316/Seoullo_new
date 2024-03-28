package com.app.data.mapper

import com.app.data.model.PlacesRequestDTO
import com.app.data.model.PlacesResponseDTO
import com.app.data.model.UserEntity
import com.app.domain.model.Places
import com.app.domain.model.PlacesRequest
import com.app.domain.model.User

/**
 * Data Entity to Data Model
 * Data Layer 에서는 Data Entity 로 받아서 사용하지만, Domain, Presentation Layer 에서는 Data Model 로 사용한다.
 * 즉, Mapper 는 Data Layer 에 존재하면서 다른 계층으로 Data 를 전달할 때, 사용하는 Data Model 에 맞춰서 변환하여 던지는 역할.
 */

fun mapperToPlaceDTO(place: PlacesRequest): PlacesRequestDTO =
    PlacesRequestDTO(
        place.includedTypes, place.maxResultCount, PlacesRequestDTO.LocationRestriction(
            circle = PlacesRequestDTO.Circle(
                center = PlacesRequestDTO.Center(place.locationRestriction.circle.center.latitude, place.locationRestriction.circle.center.longitude),
                radius = place.locationRestriction.circle.radius
            )
        )
    )

fun mapperToPlace(place: PlacesResponseDTO): List<Places> =
    place.place.toList().map {
        Places(
            it.name,
            it.id,
            Places.DisplayName(it.displayName.text)
        )
    }

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