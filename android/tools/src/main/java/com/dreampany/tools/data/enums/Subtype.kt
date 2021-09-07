package com.dreampany.tools.data.enums

import com.dreampany.framework.data.enums.BaseSubtype
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Subtype : BaseSubtype {
    DEFAULT, APPS, RATE_US, FEEDBACK, INVITE, LICENSE, ABOUT,
    WIFI, CRYPTO, RADIO, NOTE, HISTORY, NEWS,
    DETAILS, MARKET, GRAPH;

    override val value: String get() = name
}