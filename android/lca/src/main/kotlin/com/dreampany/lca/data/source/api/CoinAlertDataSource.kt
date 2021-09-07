package com.dreampany.lca.data.source.api

import com.dreampany.framework.data.source.api.DataSource
import com.dreampany.lca.data.model.CoinAlert

/**
 * Created by roman on 2019-07-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface CoinAlertDataSource : DataSource<CoinAlert> {
    fun isExists(id: String): Boolean
}