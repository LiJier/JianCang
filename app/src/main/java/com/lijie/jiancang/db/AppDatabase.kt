package com.lijie.jiancang.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lijie.jiancang.App
import com.lijie.jiancang.db.dao.CollectionDao
import com.lijie.jiancang.db.dao.ContentDao
import com.lijie.jiancang.db.dao.LabelDao
import com.lijie.jiancang.db.dao.LabelQuoteDao
import com.lijie.jiancang.db.entity.Collection
import com.lijie.jiancang.db.entity.Content
import com.lijie.jiancang.db.entity.Label
import com.lijie.jiancang.db.entity.LabelQuote

@Database(
    entities = [Collection::class, Content::class, Label::class, LabelQuote::class],
    version = 1
)
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