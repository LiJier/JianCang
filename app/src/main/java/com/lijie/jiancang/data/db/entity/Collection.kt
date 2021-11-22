package com.lijie.jiancang.data.db.entity

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

sealed class CollectionType(val type: Int) : Parcelable {
    @Parcelize
    object Text : CollectionType(0)

    @Parcelize
    object Image : CollectionType(1)

    @Parcelize
    object URL : CollectionType(2)

    @Parcelize
    object MD : CollectionType(3)
}

@Parcelize
@Entity(tableName = "collection")
data class Collection(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var type: CollectionType,
    var original: String,
    var title: String? = null,
    var content: String,
    var idea: String? = null,
    @ColumnInfo(name = "has_read")
    var hasRead: Boolean = false,
    @ColumnInfo(name = "create_time")
    var createTime: Long = System.currentTimeMillis(),
) : Parcelable

@Parcelize
data class CollectionComplete(
    @Embedded var collection: Collection,
    @Relation(
        parentColumn = "id",
        entityColumn = "collection_id"
    )
    var labelQuote: List<LabelQuote>
) : Parcelable

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
            CollectionType.MD.type -> {
                CollectionType.MD
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
            CollectionType.MD -> {
                CollectionType.MD.type
            }
        }
    }

}