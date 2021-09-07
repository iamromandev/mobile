package com.dreampany.network.nearby.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.network.nearby.data.model.Packet

/**
 * Created by roman on 7/31/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Dao
interface PacketDao : BaseDao<Packet> {
    @get:Query("select count(*) from packet")
    val count: Int

    @get:Query("select * from packet")
    val items: List<Packet>?

    @Query("select count(*) from packet where id = :id and author = :source and target = :target and type = :type and subtype = :subtype")
    fun readCount(id: String, source: String, target: String, type: String, subtype: String): Int

    @Query("select * from packet where id = :id and author = :source and target = :target and type = :type and subtype = :subtype limit 1")
    fun read(id: String, source: String, target: String, type: String, subtype: String): Packet?

    @Query("select * from packet where type = :type and subtype = :subtype")
    fun reads(type: String, subtype: String): List<Packet>?
}