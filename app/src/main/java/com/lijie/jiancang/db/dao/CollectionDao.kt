package com.lijie.jiancang.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.lijie.jiancang.db.entity.Collection

@Dao
interface CollectionDao : IDao<Collection> {

    @Query("SELECT * FROM collection")
    suspend fun queryCollections(): List<Collection>

}