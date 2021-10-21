package com.lijie.jiancang.db.entity

import androidx.room.*

sealed class CollectionType(val type: Int) {
    object Text : CollectionType(0)
    object Image : CollectionType(1)
}

@Entity(tableName = "collection")
data class Collection(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var type: CollectionType,
    var title: String? = null,
    var original: String,
    var intent: String? = null,
    var idea: String? = null,
    @ColumnInfo(name = "has_read")
    var hasRead: Boolean = false,
    @ColumnInfo(name = "create_time")
    var createTime: Long,
)

data class CollectionComplete(
    @Embedded val collection: Collection,
    @Relation(
        parentColumn = "id",
        entityColumn = "collection_id"
    )
    val content: List<Content>,
    @Relation(
        parentColumn = "id",
        entityColumn = "collection_id"
    )
    val labelQuote: List<LabelQuote>

)

class CollectionTypeConverter() {

    @TypeConverter
    fun toType(type: Int): CollectionType {
        return when (type) {
            CollectionType.Text.type -> {
                CollectionType.Text
            }
            CollectionType.Image.type -> {
                CollectionType.Image
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
        }
    }

}