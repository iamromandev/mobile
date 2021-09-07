package com.dreampany.network.nearby.data.source.room

import com.dreampany.network.nearby.data.model.Id
import com.dreampany.network.nearby.data.model.Packet
import com.dreampany.network.nearby.data.source.api.PacketDataSource
import com.dreampany.network.nearby.data.source.mapper.PacketMapper
import com.dreampany.network.nearby.data.source.room.dao.PacketDao

/**
 * Created by roman on 7/31/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class PacketRoomDataSource
constructor(
    private val mapper: PacketMapper,
    private val dao: PacketDao
) : PacketDataSource {
    @Throws
    override suspend fun isExists(
        id: Id,
        type: String,
        subtype: String
    ): Boolean = dao.readCount(id.id, id.author, id.target, type, subtype) > 0

    @Throws
    override suspend fun write(input: Packet): Long = dao.insertOrReplace(input)

    @Throws
    override suspend fun read(id: Id, type: String, subtype: String): Packet? =
        dao.read(id.id, id.author, id.target, type, subtype)

    @Throws
    override suspend fun reads(type: String, subtype: String): List<Packet>? =
        dao.reads(type, subtype)

    @Throws
    override suspend fun delete(input: Packet): Int = dao.delete(input)
}