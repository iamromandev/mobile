package com.dreampany.hi.inject

import com.dreampany.common.inject.qualifier.Nearby
import com.dreampany.common.inject.qualifier.Room
import com.dreampany.hi.data.source.api.MessageDataSource
import com.dreampany.hi.data.source.mapper.MessageMapper
import com.dreampany.hi.data.source.mapper.UserMapper
import com.dreampany.hi.data.source.nearby.MessageNearbyDataSource
import com.dreampany.hi.data.source.room.MessageRoomDataSource
import com.dreampany.hi.data.source.room.dao.MessageDao
import com.dreampany.hi.manager.NearbyManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by roman on 7/22/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Module
@InstallIn(SingletonComponent::class)
object MessageModule {
    @Nearby
    @Provides
    fun nearby(
        userMapper : UserMapper,
        messageMapper : MessageMapper,
        nearby: NearbyManager
    ): MessageDataSource = MessageNearbyDataSource( userMapper, messageMapper, nearby)

    @Room
    @Provides
    fun room(
        dao: MessageDao
    ): MessageDataSource = MessageRoomDataSource( dao)
}