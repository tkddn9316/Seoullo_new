package com.app.data.mapper

import com.app.data.model.TourInfoDTO
import com.app.data.model.UserEntity
import com.app.domain.model.TourInfo
import com.app.domain.model.User

/**
 * Data Entity to Data Model
 * Data Layer 에서는 Data Entity 로 받아서 사용하지만, Domain, Presentation Layer 에서는 Data Model 로 사용한다.
 * 즉, Mapper 는 Data Layer 에 존재하면서 다른 계층으로 Data 를 전달할 때, 사용하는 Data Model 에 맞춰서 변환하여 던지는 역할.
 */
fun mapperTOTourInfo(tourInfo: List<TourInfoDTO>): List<TourInfo> =
    tourInfo.toList().map {
        TourInfo(
            it.addr1,
            it.addr2,
            it.areacode,
            it.contentid,
            it.contenttypeid,
            it.dist,
            it.firstimage,
            it.firstimage2,
            it.mapx,
            it.mapy,
            it.mlevel,
            it.modifiedtime,
            it.sigungucode,
            it.tel,
            it.title
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