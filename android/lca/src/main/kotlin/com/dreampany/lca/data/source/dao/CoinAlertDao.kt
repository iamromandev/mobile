package com.dreampany.lca.data.source.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.frame.data.source.dao.BaseDao
import com.dreampany.lca.data.model.CoinAlert
import io.reactivex.Maybe

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Dao
interface CoinAlertDao : BaseDao<CoinAlert> {

    @get:Query("select count(*) from coinalert")
    val count: Int

    @get:Query("select count(*) from coinalert")
    val countRx: Maybe<Int>

    @get:Query("select * from coinalert")
    val items: List<CoinAlert>

    @get:Query("select * from coinalert")
    val itemsRx: Maybe<List<CoinAlert>>

    @Query("select count(*) from coinalert where id = :id limit 1")
    fun getCount(id: String): Int

    @Query("select count(*) from coinalert where id = :id limit 1")
    fun getCountRx(id: String): Maybe<Int>

    @Query("select * from coinalert where id = :id limit 1")
    fun getItem(id: String): CoinAlert

    @Query("select * from coinalert where id = :id limit 1")
    fun getItemRx(id: String): Maybe<CoinAlert>

    @Query("select * from coinalert limit :limit")
    fun getItems(limit: Int): List<CoinAlert>

    @Query("select * from coinalert limit :limit")
    fun getItemsRx(limit: Int): Maybe<List<CoinAlert>>
}
