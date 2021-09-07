package com.dreampany.common.data.source.room.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Created by roman on 7/13/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(input: T): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertOrAbort(input: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(input: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(inputs: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertOrAbort(inputs: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(inputs: List<T>): List<Long>

    @Update
    suspend fun update(input: T): Int

    @Delete
    suspend fun delete(input: T): Int
}