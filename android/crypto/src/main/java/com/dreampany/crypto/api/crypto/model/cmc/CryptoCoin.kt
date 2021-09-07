package com.dreampany.crypto.api.crypto.model.cmc

import com.dreampany.crypto.api.crypto.misc.Constants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2019-11-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CryptoCoin(
    val id: String,
    val name: String,
    val symbol: String,
    val slug: String,

    @SerializedName(Constants.Keys.CMC.RANK)
    val rank: Long,
    @SerializedName(Constants.Keys.CMC.MARKET_PAIRS)
    val marketPairs: Long,

    @SerializedName(Constants.Keys.CMC.CIRCULATING_SUPPLY)
    val circulatingSupply: Double,
    @SerializedName(Constants.Keys.CMC.TOTAL_SUPPLY)
    val totalSupply: Double,
    @SerializedName(Constants.Keys.CMC.MAX_SUPPLY)
    val maxSupply: Double,
    @SerializedName(Constants.Keys.CMC.MARKET_CAP)
    val marketCap: Double,

    @SerializedName(Constants.Keys.CMC.LAST_UPDATED)
    val lastUpdated: String,
    @SerializedName(Constants.Keys.CMC.DATE_ADDED)
    val dateAdded: String,

    val tags: List<String>,
    val platform: CryptoPlatform?,
    val quote: Map<String, CryptoQuote>
)