package com.lijie.jiancang.di

import android.content.Context
import androidx.room.Room
import com.lijie.jiancang.db.AppDatabase
import com.lijie.jiancang.repository.Repository
import com.lijie.jiancang.repository.IRepository
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
    ): IRepository {
        return Repository(db)
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