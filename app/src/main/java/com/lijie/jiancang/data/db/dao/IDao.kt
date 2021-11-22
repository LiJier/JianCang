package com.lijie.jiancang.data.db.dao

import androidx.room.*

interface IDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: T): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg data: T): List<Long>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<T>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(data: T)

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(vararg data: T)

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(list: List<T>)

    @Delete
    suspend fun delete(data: T)

    @Transaction
    @Delete
    suspend fun delete(vararg data: T)

    @Transaction
    @Delete
    suspend fun delete(list: List<T>)

}