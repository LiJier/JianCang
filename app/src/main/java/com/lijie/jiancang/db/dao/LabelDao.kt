package com.lijie.jiancang.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.lijie.jiancang.db.entity.Label

@Dao
interface LabelDao : IDao<Label> {

    @Query("SELECT * FROM label")
    suspend fun queryLabels(): List<Label>?

}