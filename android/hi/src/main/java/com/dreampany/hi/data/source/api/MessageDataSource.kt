package com.dreampany.hi.data.source.api

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dreampany.hi.data.model.Message
import com.dreampany.hi.data.model.User
import com.dreampany.hi.manager.NearbyManager
import kotlinx.coroutines.flow.Flow

/**
 * Created by roman on 7/19/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
interface MessageDataSource {
    fun register(callback: NearbyManager.Callback)

    fun unregister(callback: NearbyManager.Callback)

    suspend fun send(target: User, message: Message): Boolean

    suspend fun write(message: Message): Boolean

    suspend fun reads(page: Int, pageSize: Int): List<Message>?

    fun readsByFlow(pagingConfig: PagingConfig): Flow<PagingData<Message>>
}