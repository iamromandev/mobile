package com.dreampany.hi.data.source.nearby

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dreampany.hi.data.model.Message
import com.dreampany.hi.data.model.User
import com.dreampany.hi.data.source.api.MessageDataSource
import com.dreampany.hi.data.source.mapper.MessageMapper
import com.dreampany.hi.data.source.mapper.UserMapper
import com.dreampany.hi.manager.NearbyManager
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * Created by roman on 7/22/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class MessageNearbyDataSource(
    private val userMapper: UserMapper,
    private val messageMapper: MessageMapper,
    private val nearby: NearbyManager
) : MessageDataSource, NearbyManager.Callback {

    private val callbacks = Collections.synchronizedSet(HashSet<NearbyManager.Callback>())

    override fun register(callback: NearbyManager.Callback) {
        callbacks.add(callback)
        nearby.register(this)
    }

    override fun unregister(callback: NearbyManager.Callback) {
        callbacks.remove(callback)
        nearby.unregister(this)
    }

    override suspend fun send(target: User, message: Message) = nearby.send(target, message)

    override suspend fun write(message: Message): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun reads(page: Int, pageSize: Int): List<Message>? {
        TODO("Not yet implemented")
    }

    override fun readsByFlow(pagingConfig: PagingConfig): Flow<PagingData<Message>> {
        TODO("Not yet implemented")
    }


    override fun onUser(user: User, nearby: Boolean, internet: Boolean) {
        callbacks.forEach {
            it.onUser(user, nearby, internet)
        }
    }

    override fun onMessage(user: User, message: Message) {
        callbacks.forEach {
            it.onMessage(user, message)
        }
    }
}