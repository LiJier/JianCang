package com.lijie.jiancang.db.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "label")
data class Label(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String,
) {
    @Ignore
    var check: Boolean = false
}
