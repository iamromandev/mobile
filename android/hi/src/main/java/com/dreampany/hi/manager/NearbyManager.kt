package com.dreampany.hi.manager

import android.content.Context
import com.dreampany.common.misc.exts.applicationId
import com.dreampany.hi.data.model.Message
import com.dreampany.hi.data.model.User
import com.dreampany.hi.data.source.mapper.MessageMapper
import com.dreampany.hi.data.source.mapper.UserMapper
import com.dreampany.hi.data.source.pref.Pref
import com.dreampany.network.nearby.NearbyApi
import com.dreampany.network.nearby.data.model.Id
import com.dreampany.network.nearby.data.model.Peer
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 7/23/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Singleton
class NearbyManager
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val pref: Pref,
    private val userMapper: UserMapper,
    private val messageMapper: MessageMapper,
    private val nearby: NearbyApi
) : NearbyApi.Callback {

    interface Callback {
        fun onUser(user: User, nearby: Boolean, internet: Boolean)
        fun onMessage(user: User, message: Message)
    }

    private val callbacks = Collections.synchronizedSet(HashSet<Callback>())

    @Volatile
    private var inited = false

    override fun onPeer(peer: Peer, state: Peer.State) {
        val user = userMapper.read(peer)
        Timber.v("%s:%s", peer, user)
        callbacks.forEach {
            it.onUser(user, nearby = state == Peer.State.LIVE, internet = false)
        }
    }

    override fun onData(peer: Peer, data: ByteArray) {
        val user = userMapper.read(peer)
        val message = messageMapper.parse(data) ?: return
        Timber.v(message.toString())
        callbacks.forEach { it.onMessage(user, message) }
    }

    override fun onStatus(
        payloadId: Long,
        state: NearbyApi.PayloadState,
        totalBytes: Long,
        bytesTransferred: Long
    ) {
    }

    fun register(callback: Callback) {
        callbacks.add(callback)
        nearby.register(this)
        handleNearby()
    }

    fun unregister(callback: Callback) {
        callbacks.remove(callback)
        nearby.unregister(this)
        handleNearby()
    }

    fun send(target: User, message: Message): Boolean {
        val id = Id(message.id, message.author, target.id)
        val data = messageMapper.convert(message) ?: return false
        return nearby.send(id, data)
    }

    private fun handleNearby() {
        if (callbacks.isEmpty())
            nearby.stop()
        else {
            if (!inited) {
                // TODO should handle also re inited
                val serviceId = context.applicationId
                val user = pref.user ?: return
                // TODO NearbyApi.Type should come from pref
                inited =
                    nearby.init(
                        NearbyApi.Strategy.PTP,
                        serviceId,
                        user.id,
                        userMapper.convert(user)
                    )
            }
            nearby.start()
        }
    }

}