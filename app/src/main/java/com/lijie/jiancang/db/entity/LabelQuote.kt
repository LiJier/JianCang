package com.lijie.jiancang.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "label_quote")
data class LabelQuote(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val collectionId: Long,
    val labelId: Long,
    var labelName: String
)
