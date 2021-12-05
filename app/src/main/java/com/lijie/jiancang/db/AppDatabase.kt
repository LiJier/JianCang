package com.lijie.jiancang.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lijie.jiancang.db.dao.CollectionDao
import com.lijie.jiancang.db.dao.LabelDao
import com.lijie.jiancang.db.dao.LabelQuoteDao
import com.lijie.jiancang.db.entity.Collection
import com.lijie.jiancang.db.entity.CollectionTypeConverter
import com.lijie.jiancang.db.entity.Label
import com.lijie.jiancang.db.entity.LabelQuote

@Database(
    entities = [Collection::class, Label::class, LabelQuote::class],
    version = 1
)
@TypeConverters(value = [CollectionTypeConverter::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun collectionDao(): CollectionDao
    abstract fun labelDao(): LabelDao
    abstract fun labelQuoteDao(): LabelQuoteDao

}