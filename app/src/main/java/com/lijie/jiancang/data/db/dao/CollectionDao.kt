package com.lijie.jiancang.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.lijie.jiancang.data.db.entity.Collection
import com.lijie.jiancang.data.db.entity.CollectionComplete

@Dao
interface CollectionDao : IDao<Collection> {

    @Transaction
    @Query("SELECT * FROM collection")
    suspend fun queryCollections(): List<CollectionComplete>

}