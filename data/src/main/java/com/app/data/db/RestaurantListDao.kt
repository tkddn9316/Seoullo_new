package com.app.data.db

import androidx.paging.PagingSource
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.data.db.entity.RestaurantEntity
import com.app.data.model.PlacesResponseDTO

interface RestaurantListDao {
    // 전체 목록을 PagingSource로 가져온다.(최신 수정일 순으로)
    @Query("SELECT * FROM Restaurant ORDER BY modifiedtime DESC")
    fun getPlaces(): PagingSource<Int, RestaurantEntity>

//    @Query("SELECT * FROM Restaurant ORDER BY modifiedtime DESC")
//    fun getPlaces(): PagingSource<Int, RestaurantEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: PlacesResponseDTO.Place)

    @Query("DELETE FROM Restaurant")
    fun deleteAll()
}