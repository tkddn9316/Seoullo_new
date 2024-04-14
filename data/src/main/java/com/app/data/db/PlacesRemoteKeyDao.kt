package com.app.data.db

import androidx.room.Query

/**
 * Local DB를 사용하는 paging3는 RemoteKey를 제공하지 않으므로 따로 만들어줘야 함
 */
interface PlacesRemoteKeyDao {
    @Query("SELECT `key` FROM places_remote_keys WHERE cat1 = :cat1 AND cat2 = :cat2 AND cat3 = :cat3 AND contentid = :contentId")
    fun getKey(cat1: String, cat2: String, cat3: String, contentId: String)

    @Query("DELETE FROM places_remote_keys WHERE cat1 = :cat1 AND cat2 = :cat2 AND cat3 = :cat3 AND contentid = :contentId")
    fun deleteKey(cat1: String, cat2: String, cat3: String, contentId: String)
}