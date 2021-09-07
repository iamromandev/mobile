package com.dreampany.network.nearby.data.source.api

import com.dreampany.network.nearby.data.model.Id
import com.dreampany.network.nearby.data.model.Packet

/**
 * Created by roman on 7/31/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
interface PacketDataSource {
    @Throws
    suspend fun isExists(id: Id, type: String, subtype: String): Boolean

    @Throws
    suspend fun write(input: Packet): Long

    @Throws
    suspend fun read(id: Id, type: String, subtype: String): Packet?

    @Throws
    suspend fun reads(type: String, subtype: String): List<Packet>?

    @Throws
    suspend fun delete(input: Packet): Int
}