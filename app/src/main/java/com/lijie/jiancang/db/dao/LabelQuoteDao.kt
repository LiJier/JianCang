package com.lijie.jiancang.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.lijie.jiancang.db.entity.LabelQuote

@Dao
interface LabelQuoteDao : IDao<LabelQuote> {

    @Transaction
    @Query("DELETE FROM label_quote WHERE label_id = :labelId")
    suspend fun deleteLabel(labelId: Long)

}