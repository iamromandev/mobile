package com.dreampany.word.data.source.room

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.framework.data.source.room.dao.BaseDao
import com.dreampany.word.data.model.Synonym
import io.reactivex.Maybe


/**
 * Created by Hawladar Roman on 9/5/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Dao
interface SynonymDao : BaseDao<Synonym> {
    @get:Query("select count(*) from synonym")
    val count: Int

    @get:Query("select count(*) from synonym")
    val countRx: Maybe<Int>

    @get:Query("select * from synonym")
    val items: List<Synonym>

    @get:Query("select * from synonym")
    val itemsRx: Maybe<List<Synonym>>

    @Query("select * from synonym where lefter = :word or righter = :word")
    fun getItems(word: String): List<Synonym>

    @Query("select * from synonym where lefter = :word or righter = :word")
    fun getItemsRx(word: String): Maybe<List<Synonym>>
}