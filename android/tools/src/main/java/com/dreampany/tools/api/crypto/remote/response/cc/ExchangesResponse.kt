package com.dreampany.tools.api.crypto.remote.response.cc

import com.dreampany.tools.api.crypto.misc.Constants
import com.dreampany.tools.api.crypto.model.cc.ExchangesData
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class ExchangesResponse(
    @SerializedName(Constants.Keys.Exchange.DATA)
    val data: ExchangesData
)