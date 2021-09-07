package com.dreampany.hi.data.source.room

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dreampany.hi.data.model.Message
import com.dreampany.hi.data.model.User
import com.dreampany.hi.data.source.api.MessageDataSource
import com.dreampany.hi.data.source.room.dao.MessageDao
import com.dreampany.hi.manager.NearbyManager
import kotlinx.coroutines.flow.Flow

/**
 * Created by roman on 8/21/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class MessageRoomDataSource(
    private val dao: MessageDao,
) : MessageDataSource {

    override fun register(callback: NearbyManager.Callback) {
        TODO("Not yet implemented")
    }

    override fun unregister(callback: NearbyManager.Callback) {
        TODO("Not yet implemented")
    }

    override suspend fun send(target: User, message: Message): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun write(message: Message) = dao.insertOrReplace(message) > 0

    override suspend fun reads(page: Int, pageSize: Int) = dao.reads(page, pageSize)

    override fun readsByFlow(pagingConfig: PagingConfig): Flow<PagingData<Message>> =
        Pager<Int, Message>(pagingConfig) { dao.page }.flow


}