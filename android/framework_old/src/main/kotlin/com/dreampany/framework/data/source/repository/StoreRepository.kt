package com.dreampany.framework.data.source.repository

import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Store
import com.dreampany.framework.data.source.api.StoreDataSource
import com.dreampany.framework.misc.ResponseMapper
import com.dreampany.framework.injector.annote.Room
import com.dreampany.framework.misc.RxMapper
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-25
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class StoreRepository
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    @Room private val room: StoreDataSource
) : Repository<String, Store>(rx, rm), StoreDataSource {
    override fun getItems(type: Type, subtype: Subtype, state: State, limit: Long): List<Store>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(
        type: Type,
        subtype: Subtype,
        state: State,
        limit: Long
    ): Maybe<List<Store>> {
        return room.getItemsRx(type, subtype, state, limit)
    }

    override fun getRandomItem(type: Type, subtype: Subtype, state: State, exclude: State): Store? {
        return room.getRandomItem(type, subtype, state, exclude)
    }

    override fun getRandomItem(type: Type, subtype: Subtype, state: State): Store? {
        return room.getRandomItem(type, subtype, state)
    }

    override fun getItem(type: Type, subtype: Subtype, state: State): Store? {
        return room.getItem(type, subtype, state)
    }

    override fun getItems(type: Type, subtype: Subtype, state: State): List<Store>? {
        return room.getItems(type, subtype, state)
    }

    override fun getItemsRx(type: Type, subtype: Subtype, state: State): Maybe<List<Store>> {
        return room.getItemsRx(type, subtype, state)
    }

    override fun getCount(id: String, type: Type, subtype: Subtype, state: State): Int {
        return room.getCount(id, type, subtype, state)
    }

    override fun isExists(id: String, type: Type, subtype: Subtype, state: State): Boolean {
        return room.isExists(id, type, subtype, state)
    }

    override fun isExists(id: String, type: Type, subtype: Subtype, states: Array<State>): Boolean {
        return room.isExists(id, type, subtype, states)
    }

    override fun isExistsRx(
        id: String,
        type: Type,
        subtype: Subtype,
        state: State
    ): Maybe<Boolean> {
        return room.isExistsRx(id, type, subtype, state)
    }

    override fun getCount(id: String, type: Type, subtype: Subtype): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCountRx(id: String, type: Type, subtype: Subtype): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCountRx(): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCountByType(type: Type, subtype: Subtype, state: State): Int {
        return room.getCountByType(type, subtype, state)
    }

    override fun getCountByTypeRx(type: Type, subtype: Subtype, state: State): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String, type: Type, subtype: Subtype, state: State): Store? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItem(t: Store): Long {
        return room.putItem(t)
    }

    override fun putItems(ts: List<Store>): List<Long>? {
        return room.putItems(ts)
    }

    override fun delete(t: Store): Int {
        return room.delete(t)
    }

    override fun deleteRx(t: Store): Maybe<Int> {
        return room.deleteRx(t)
    }

    override fun getItem(id: String): Store? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(id: String, type: Type, subtype: Subtype, state: State): Maybe<Store> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmpty(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExists(t: Store): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExistsRx(t: Store): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemRx(t: Store): Maybe<Long> {
        return room.putItemRx(t)
    }

    override fun putItemsRx(ts: List<Store>): Maybe<List<Long>> {
        return room.putItemsRx(ts)
    }

    override fun delete(ts: List<Store>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<Store>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(id: String): Maybe<Store> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(): List<Store>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(): Maybe<List<Store>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Long): List<Store>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Long): Maybe<List<Store>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}