package com.lijie.jiancang.db.entity

import androidx.room.*

sealed class CollectionType(val type: Int) {
    object Text : CollectionType(0)
    object Image : CollectionType(1)
    object URL : CollectionType(2)
}

@Entity(tableName = "collection")
data class Collection(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var type: CollectionType,
    var title: String? = null,
    var content: String,
    var idea: String? = null,
    @ColumnInfo(name = "has_read")
    var hasRead: Boolean = false,
    @ColumnInfo(name = "create_time")
    var createTime: Long = System.currentTimeMillis(),
)

data class CollectionComplete(
    @Embedded var collection: Collection,
    @Relation(
        parentColumn = "id",
        entityColumn = "collection_id"
    )
    var labelQuote: List<LabelQuote>
)

class CollectionTypeConverter {

    @TypeConverter
    fun toType(type: Int): CollectionType {
        return when (type) {
            CollectionType.Text.type -> {
                CollectionType.Text
            }
            CollectionType.Image.type -> {
                CollectionType.Image
            }
            CollectionType.URL.type -> {
                CollectionType.URL
            }
            else -> {
                CollectionType.Text
            }
        }
    }

    @TypeConverter
    fun toInt(type: CollectionType): Int {
        return when (type) {
            CollectionType.Text -> {
                CollectionType.Text.type
            }
            CollectionType.Image -> {
                CollectionType.Image.type
            }
            CollectionType.URL -> {
                CollectionType.URL.type
            }
        }
    }

}