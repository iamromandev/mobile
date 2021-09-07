package com.dreampany.tools.data.source.crypto.remote

import android.content.Context
import com.dreampany.framework.misc.exts.isDebug
import com.dreampany.framework.misc.func.Keys
import com.dreampany.framework.misc.func.Parser
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.crypto.remote.service.CryptoCompareService
import com.dreampany.tools.data.model.crypto.Exchange
import com.dreampany.tools.data.source.crypto.api.ExchangeDataSource
import com.dreampany.tools.data.source.crypto.mapper.ExchangeMapper
import com.dreampany.tools.misc.constants.Constants
import com.google.common.collect.Maps
import java.net.UnknownHostException

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ExchangeRemoteDataSource
constructor(
    private val context: Context,
    private val network: NetworkManager,
    private val parser: Parser,
    private val keys: Keys,
    private val mapper: ExchangeMapper,
    private val service: CryptoCompareService
) : ExchangeDataSource {

    init {
        if (context.isDebug) {
            keys.setKeys(Constants.Apis.CryptoCompare.API_KEY_ROMAN_BJIT)
        } else {
            keys.setKeys(Constants.Apis.CryptoCompare.API_KEY_ROMAN_BJIT)
        }
    }

    @Throws
    override suspend fun getExchanges(
        fromSymbol: String,
        toSymbol: String,
        extraParams: String,
        limit: Long
    ): List<Exchange>? {
        for (index in 0..keys.length) {
            try {
                val key = keys.nextKey ?: continue
                val response = service.getExchanges(
                    key.header,
                    fromSymbol,
                    toSymbol,
                    extraParams,
                    limit
                ).execute()
                if (response.isSuccessful) {
                    val data = response.body()?.data?.exchanges ?: return null
                    return mapper.getItems(data)
                } else {
                    //val error = parser.parseError(response, TradesResponse::class)
                    throw SmartError()
                }
            } catch (error: Throwable) {
                if (error is SmartError) throw error
                if (error is UnknownHostException) throw SmartError(
                    message = error.message,
                    error = error
                )
                keys.randomForwardKey()
            }
        }
        throw SmartError()
    }

    private val String.header: Map<String, String>
        get() {
            val header = Maps.newHashMap<String, String>()
            header.put(
                Constants.Apis.CoinMarketCap.ACCEPT,
                Constants.Apis.CoinMarketCap.ACCEPT_JSON
            )
            header.put(Constants.Apis.CryptoCompare.AUTHORIZATION, this)
            return header
        }
}