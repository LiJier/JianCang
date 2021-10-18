package com.lijie.jiancang.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collection")
data class Collection(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var type: Int,
    var title: String?,
    var original: String,
    var content: String,
    var intent: String,
    var idea: String?,
    var createTime: Long,
)