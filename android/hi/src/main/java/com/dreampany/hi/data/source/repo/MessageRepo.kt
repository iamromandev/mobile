package com.dreampany.hi.data.source.repo

import androidx.paging.PagingConfig
import com.dreampany.common.inject.qualifier.Nearby
import com.dreampany.common.inject.qualifier.Room
import com.dreampany.hi.data.model.Message
import com.dreampany.hi.data.model.User
import com.dreampany.hi.data.source.api.MessageDataSource
import com.dreampany.hi.manager.NearbyManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 7/22/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Singleton
class MessageRepo
@Inject constructor(
    @Nearby private val nearby: MessageDataSource,
    @Room private val room: MessageDataSource
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

    override suspend fun send(target: User, message: Message) = withContext(Dispatchers.IO) {
        room.write(message)
        nearby.send(target, message)
    }

    override suspend fun write(message: Message): Boolean = room.write(message)

    override suspend fun reads(page: Int, pageSize: Int) = room.reads(page, pageSize)

    override fun readsByFlow(pagingConfig: PagingConfig) = room.readsByFlow(pagingConfig)

    override fun onUser(user: User, nearby: Boolean, internet: Boolean) {
        callbacks.forEach {
            it.onUser(user, nearby, internet)
        }
    }

    override fun onMessage(user: User, message: Message) {
        callbacks.forEach { it.onMessage(user, message) }
    }
}