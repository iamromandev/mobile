package com.dreampany.hi.data.source.api

import com.dreampany.hi.data.model.User
import com.dreampany.hi.manager.NearbyManager

/**
 * Created by roman on 7/10/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
interface UserDataSource {

    @Throws
    fun register(callback: NearbyManager.Callback)

    @Throws
    fun unregister(callback: NearbyManager.Callback)

    @Throws
    suspend fun isFavorite(input: User): Boolean

    @Throws
    suspend fun toggleFavorite(input: User): Boolean

    @Throws
    suspend fun readFavorites(): List<User>?

    @Throws
    suspend fun write(input: User): Long

    @Throws
    suspend fun write(inputs: List<User>): List<Long>?

    @Throws
    suspend fun read(id: String): User?

    @Throws
    suspend fun reads(): List<User>?

    @Throws
    suspend fun reads(ids: List<String>): List<User>?

    @Throws
    suspend fun reads(offset: Long, limit: Long): List<User>?
}