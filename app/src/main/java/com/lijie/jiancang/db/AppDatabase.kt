package com.lijie.jiancang.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lijie.jiancang.App
import com.lijie.jiancang.db.dao.CollectionDao
import com.lijie.jiancang.db.dao.ContentDao
import com.lijie.jiancang.db.dao.LabelDao
import com.lijie.jiancang.db.dao.LabelQuoteDao
import com.lijie.jiancang.db.entity.*
import com.lijie.jiancang.db.entity.Collection

@Database(
    entities = [Collection::class, Content::class, Label::class, LabelQuote::class],
    version = 1
)
@TypeConverters(value = [CollectionTypeConverter::class, ContentTypeConverter::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun collectionDao(): CollectionDao
    abstract fun contentDao(): ContentDao
    abstract fun labelDao(): LabelDao
    abstract fun labelQuoteDao(): LabelQuoteDao

    companion object {
        val db by lazy {
            Room.databaseBuilder(
                App.appContext,
                AppDatabase::class.java, "jian-cang.db"
            ).build()
        }
    }

}