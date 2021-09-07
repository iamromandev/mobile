package com.dreampany.hi.data.source.mapper

import com.dreampany.common.misc.func.GsonParser
import com.dreampany.hi.data.model.Message
import com.dreampany.hi.misc.exts.isTextMessage
import com.dreampany.hi.misc.exts.textMessagePacket
import com.dreampany.network.nearby.core.Packets
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 7/22/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Singleton
class MessageMapper
@Inject constructor(
    private val parser: GsonParser
) {

    @Synchronized
    fun parse(data: ByteArray): Message? {
        if (data.isTextMessage)
            return parseTextMessage(Packets.copyToBuffer(data, 1).array())

        return null
    }

    @Synchronized
    fun convert(message: Message): ByteArray? {

        val json = parser.toJson<Message>(message)
        val data = json.toByteArray()
        return data.textMessagePacket
    }

    private fun parseTextMessage(data: ByteArray): Message? {
        try {
            val json = String(data)
            return parser.fromJson<Message>(json)
        } catch (error: Throwable) {
            Timber.e(error)
            return null
        }
    }
}