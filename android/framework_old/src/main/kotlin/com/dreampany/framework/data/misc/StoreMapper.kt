package com.dreampany.framework.data.misc

import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Store
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.framework.injector.annote.StoreAnnote
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-25
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class StoreMapper
@Inject constructor(
    @StoreAnnote private val map: SmartMap<String, Store>,
    @StoreAnnote private val cache: SmartCache<String, Store>
) : Mapper() {

    fun isExists(id: String): Boolean {
        return map.contains(id)
    }

    fun isExists(id: String, type: Type, subtype: Subtype, state: State): Boolean {
        if (isExists(id)) {
            val item = getItem(id)
            return item.hasProperty(type, subtype, state)
        }
        return false
    }

    fun putItem(item: Store) {
        map.put(item.id, item)
    }

    fun getItem(id: String): Store {
        return map.get(id)
    }

    fun getItem(id: String, type: Type, subtype: Subtype, state: State, extra: String? = null): Store {
        return if (isExists(id, type, subtype, state)) getItem(id)
        else Store(
            id = id,
            type = type,
            subtype = subtype,
            state = state,
            extra = extra
        )
    }
}