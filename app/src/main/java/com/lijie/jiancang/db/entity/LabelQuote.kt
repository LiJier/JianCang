package com.lijie.jiancang.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "label_quote")
data class LabelQuote(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val collectionId: Int,
    val labelId: Int,
    var labelName: String
)
