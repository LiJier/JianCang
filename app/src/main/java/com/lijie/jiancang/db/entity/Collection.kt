package com.lijie.jiancang.db.entity

import androidx.room.*

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