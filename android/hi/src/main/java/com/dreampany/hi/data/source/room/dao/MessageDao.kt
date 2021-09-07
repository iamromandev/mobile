package com.dreampany.hi.data.source.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.dreampany.common.data.source.room.dao.BaseDao
import com.dreampany.hi.data.model.Message

/**
 * Created by roman on 8/21/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Dao
interface MessageDao : BaseDao<Message> {
    @Query("select * from message order by created_at desc limit :pageSize offset (:page-1) * :pageSize")
    suspend fun reads(page: Int, pageSize: Int): List<Message>?

    @get:Query("select * from message order by created_at desc")
    val page: PagingSource<Int, Message>
}