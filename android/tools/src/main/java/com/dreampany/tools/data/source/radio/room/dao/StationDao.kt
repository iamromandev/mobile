package com.dreampany.tools.data.source.radio.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.tools.data.model.radio.Station

/**
 * Created by roman on 21/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Dao
interface StationDao : BaseDao<Station> {
    @get:Query("select count(*) from station")
    val count: Int

    @get:Query("select * from station order by last_check_ok_time desc")
    val items: List<Station>?

    @Query("select * from station where country_code = :countryCode order by last_check_ok_time desc limit :limit")
    fun getItems(countryCode: String, limit: Long): List<Station>?

    @Query("select * from station order by click_count desc limit :limit")
    fun getItemsOfTrends(limit: Long): List<Station>?

    @Query("select * from station order by votes desc limit :limit")
    fun getItemsOfPopular(limit: Long): List<Station>?
}