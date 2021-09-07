package com.dreampany.framework.data.source.room

import com.dreampany.framework.data.model.Store
import com.dreampany.framework.data.source.api.StoreDataSource
import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.room.dao.StoreDao

/**
 * Created by roman on 1/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class StoreRoomDataSource
constructor(
    private val mapper: StoreMapper,
    private val dao: StoreDao
) : StoreDataSource {

    @Throws
    override suspend fun isExists(
        id: String,
        type: String,
        subtype: String,
        state: String
    ): Boolean = dao.readCount(id, type, subtype, state) > 0

    @Throws
    override suspend fun write(item: Store): Long = dao.insertOrReplace(item)

    @Throws
    override suspend fun read(
        id: String,
        type: String,
        subtype: String,
        state: String
    ): Store? = dao.read(id, type, subtype, state)

    @Throws
    override suspend fun reads(type: String, subtype: String, state: String): List<Store>? =
        dao.reads(type, subtype, state)

    @Throws
    override suspend fun delete(store: Store): Int = dao.delete(store)
}