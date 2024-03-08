package com.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.app.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: UserEntity)

    @Query("SELECT * FROM user")
    fun getAll(): Flow<List<UserEntity>>

    @Update
    fun update(data: UserEntity)

    @Query("DELETE FROM user")
    fun deleteAll()
}