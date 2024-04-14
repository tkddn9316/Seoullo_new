package com.app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Restaurant")
data class RestaurantEntity(
    @PrimaryKey val id: Long,
    override val addr1: String,
    override val addr2: String,
    override val areacode: String,
    override val cat1: String,
    override val cat2: String,
    override val cat3: String,
    override val contentid: String,
    override val contenttypeid: String,
    override val createdtime: String,
    override val dist: String,
    override val firstimage: String,
    override val firstimage2: String,
    override val mapx: String,
    override val mapy: String,
    override val mlevel: String,
    override val modifiedtime: String,
    override val sigungucode: String,
    override val tel: String,
    override val title: String,
) : BaseEntity()
