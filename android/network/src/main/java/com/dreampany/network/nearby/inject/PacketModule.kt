package com.dreampany.network.nearby.inject

import com.dreampany.network.nearby.data.source.api.PacketDataSource
import com.dreampany.network.nearby.data.source.mapper.PacketMapper
import com.dreampany.network.nearby.data.source.room.PacketRoomDataSource
import com.dreampany.network.nearby.data.source.room.dao.PacketDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by roman on 7/31/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Module
@InstallIn(SingletonComponent::class)
object PacketModule {
    @Room
    @Provides
    fun room(
        mapper: PacketMapper,
        dao: PacketDao
    ): PacketDataSource = PacketRoomDataSource(mapper, dao)
}