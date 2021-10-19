package com.lijie.jiancang.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

sealed class CollectionType(val type: Int) {
    object Text : CollectionType(0)
}

@Entity(tableName = "collection")
data class Collection(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var type: Int,
    var title: String? = null,
    var original: String,
    var content: String,
    var intent: String? = null,
    var idea: String? = null,
    var createTime: Long,
)