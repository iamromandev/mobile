package com.dreampany.hi.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.common.data.source.room.dao.BaseDao
import com.dreampany.hi.data.model.File

/**
 * Created by roman on 8/28/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Dao
interface FileDao : BaseDao<File> {
    @Query("select * from file order by created_at desc limit :pageSize offset :page * :pageSize")
    suspend fun reads(page: Int, pageSize: Int): List<File>?
}