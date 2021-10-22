package com.lijie.jiancang.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

sealed class ContentType(val type: String) {
    object Text : ContentType("text")
    object Image : ContentType("image")
}

@Entity(tableName = "content")
data class Content(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "collection_id")
    var collectionId: Long = 0,
    var type: ContentType,
    var content: String,
    var sort: Int
)

class ContentTypeConverter() {

    @TypeConverter
    fun toType(type: String): ContentType {
        return when (type) {
            ContentType.Text.type -> {
                ContentType.Text
            }
            ContentType.Image.type -> {
                ContentType.Image
            }
            else -> {
                ContentType.Text
            }
        }
    }

    @TypeConverter
    fun toName(type: ContentType): String {
        return when (type) {
            ContentType.Text -> {
                ContentType.Text.type
            }
            ContentType.Image -> {
                ContentType.Image.type
            }
        }
    }

}
