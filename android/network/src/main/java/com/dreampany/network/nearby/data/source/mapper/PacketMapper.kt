package com.dreampany.network.nearby.data.source.mapper

import com.dreampany.network.nearby.data.model.Id
import com.dreampany.network.nearby.data.model.Packet
import com.google.common.collect.Maps
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 7/31/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Singleton
class PacketMapper
@Inject constructor() {

    private val packets: MutableMap<Id, Packet>

    init {
        packets = Maps.newConcurrentMap()
    }

    fun isExists(id: Id): Boolean = packets.contains(id)

    fun isExists(
        id: Id,
        type: String,
        subtype: String
    ): Boolean {
        if (isExists(id)) {
            val item = read(id)
            return item?.hasProperty(type, subtype) ?: false
        }
        return false
    }

    fun write(input: Packet) = packets.put(input.id, input)

    fun read(id: Id): Packet? {
        return packets.get(id)
    }

    fun read(
        id: Id,
        type: String,
        subtype: String
    ): Packet? {
        return if (isExists(id, type, subtype)) read(id)
        else Packet(id = id)
    }
}