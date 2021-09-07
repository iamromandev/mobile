package com.dreampany.tube.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.tube.data.model.Category

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface CategoryDao : BaseDao<Category> {

    @get:Query("select * from category")
    val all: List<Category>?

    @Query("select * from category where id = :id limit 1")
    fun read(id: String): Category?

    @Query("delete from category")
    fun deleteAll()
}