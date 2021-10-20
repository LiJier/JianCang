package com.lijie.jiancang.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "label_quote")
data class LabelQuote(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "collection_id")
    val collectionId: Long = 0,
    @ColumnInfo(name = "label_id")
    val labelId: Long = 0,
    @ColumnInfo(name = "label_name")
    var labelName: String
)
