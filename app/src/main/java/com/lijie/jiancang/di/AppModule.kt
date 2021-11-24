package com.lijie.jiancang.di

import android.content.Context
import androidx.room.Room
import com.lijie.jiancang.data.db.AppDatabase
import com.lijie.jiancang.data.source.CollectionRepository
import com.lijie.jiancang.data.source.ICollectionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCollectionRepository(
        db: AppDatabase
    ): ICollectionRepository {
        return CollectionRepository(db)
    }

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "jian-cang.db"
        ).build()
    }

}