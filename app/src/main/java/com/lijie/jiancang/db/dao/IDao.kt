package com.lijie.jiancang.db.dao

import androidx.room.*

interface IDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: T)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg data: T)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<T>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(data: T)

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg data: T)

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