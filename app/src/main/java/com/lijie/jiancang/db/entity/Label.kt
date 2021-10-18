package com.lijie.jiancang.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "label")
data class Label(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String
)
