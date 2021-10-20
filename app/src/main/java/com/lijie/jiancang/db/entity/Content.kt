package com.lijie.jiancang.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

sealed class ContentType(val type: String) {
    object Text : ContentType("text")
}

@Entity(tableName = "content")
data class Content(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "collection_id")
    val collectionId: Long = 0,
    var type: String,
    var content: String,
    var sort: Int
)
