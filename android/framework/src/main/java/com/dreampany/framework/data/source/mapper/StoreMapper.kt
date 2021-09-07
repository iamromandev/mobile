package com.dreampany.framework.data.source.mapper

import com.dreampany.framework.data.model.Store
import com.google.common.collect.Maps
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 1/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class StoreMapper
@Inject constructor() {

    private val stores: MutableMap<String, Store>

    init {
        stores = Maps.newConcurrentMap()
    }

    fun isExists(id: String): Boolean {
        return stores.contains(id)
    }

    fun isExists(id: String, type: String, subtype: String, state: String): Boolean {
        if (isExists(id)) {
            val item = readStore(id)
            return item?.hasProperty(type, subtype, state) ?: false
        }
        return false
    }

    fun putItem(item: Store) {
        stores.put(item.id, item)
    }

    fun readStore(id: String): Store? {
        return stores.get(id)
    }

    fun readStore(id: String, type: String, subtype: String, state: String, extra: String? = null): Store? {
        return if (isExists(id, type, subtype, state)) readStore(id)
        else Store(
            id = id,
            type = type,
            subtype = subtype,
            state = state,
            extra = extra
        )
    }
}