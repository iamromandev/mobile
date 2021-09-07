package com.dreampany.lca.data.source.dao


import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.lca.data.model.Coin
import io.reactivex.Maybe

/**
 * Created by Hawladar Roman on 4/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@Dao
interface CoinDao : BaseDao<Coin> {

    @get:Query("select count(*) from coin")
    val count: Int

    @get:Query("select count(*) from coin")
    val countRx: Maybe<Int>

    @get:Query("select * from coin order by rank asc")
    val items: List<Coin>

    @get:Query("select * from coin order by rank asc")
    val itemsRx: Maybe<List<Coin>>

    @Query("select count(*) from coin where id = :id limit 1")
    fun getCount(id: String): Int

    @Query("select count(*) from coin where id = :id limit 1")
    fun getCountRx(id: String): Maybe<Int>

    @Query("select * from coin where id = :id limit 1")
    fun getItemById(id: String): Coin

    @Query("select * from coin where id = :id limit 1")
    fun getItemRx(id: String): Maybe<Coin>

    @Query("select * from coin where symbol = :symbol limit 1")
    fun getItem(symbol: String): Coin

    @Query("select * from coin where symbol = :symbol and lastUpdated  <= :lastUpdated limit 1")
    fun getItem(symbol: String, lastUpdated: Long): Coin

    @Query("select * from coin where symbol = :symbol and lastUpdated  <= :lastUpdated limit 1")
    fun getItemRx(symbol: String, lastUpdated: Long): Maybe<Coin>

    @Query("select * from coin where rank >= :start order by rank asc")
    fun getItems(start: Int): List<Coin>

    @Query("select * from coin where rank >= :start order by rank asc")
    fun getItemsRx(start: Int): Maybe<List<Coin>>

    @Query("select * from coin where rank >= :start and rank < :end order by rank asc limit :limit")
    fun getItems(start: Int, end: Int, limit: Int): List<Coin>

    @Query("select * from coin where symbol in (:symbols) order by rank asc")
    fun getItems(symbols: List<String>): List<Coin>

    @Query("select * from coin where rank >= :start order by rank asc limit :limit")
    fun getItemsRx(start: Int, limit: Int): Maybe<List<Coin>>

    @Query("select * from coin where rank >= :start and rank < :end order by rank asc limit :limit")
    fun getItemsRx(start: Int, end: Int, limit: Int): Maybe<List<Coin>>
}
