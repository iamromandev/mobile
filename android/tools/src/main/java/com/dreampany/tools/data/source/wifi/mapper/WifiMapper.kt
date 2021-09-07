package com.dreampany.tools.data.source.wifi.mapper

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.misc.exts.sub
import com.dreampany.network.misc.centerFreq0
import com.dreampany.tools.data.enums.wifi.Band
import com.dreampany.tools.data.enums.wifi.Width
import com.dreampany.tools.data.model.wifi.Signal
import com.dreampany.tools.data.model.wifi.Wifi
import com.dreampany.tools.data.source.wifi.api.WifiDataSource
import com.dreampany.tools.data.source.wifi.pref.Prefs
import com.dreampany.tools.misc.utils.WifiUtils
import com.google.common.collect.Maps
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList
import kotlin.math.abs

/**
 * Created by roman on 24/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class WifiMapper
@Inject constructor(
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val pref: Prefs
) {
    private val wifis: MutableMap<String, Wifi>

    init {
        wifis = Maps.newConcurrentMap()
    }

    @Synchronized
    fun add(wifi: Wifi) = wifis.put(wifi.id, wifi)

    @Throws
    @Synchronized
    suspend fun gets(
        source: WifiDataSource,
        offset: Long,
        limit: Long,
        callback: () -> Unit
    ): List<Wifi>? {
        updateCache(source, callback)
        val cache = sortedWifis(wifis.values.toList())
        val result = sub(cache, offset, limit)
        return result
    }

    @Synchronized
    fun gets(inputs: List<ScanResult>, info: WifiInfo?): List<Wifi> {
        val result = arrayListOf<Wifi>()
        val active = if (info == null) null else get(info)
        inputs.forEach { input ->
            val wifi = get(input)
            if (wifi == active) {
                wifi.speed = active.speed
            }
            result.add(wifi)
        }
        return result
    }

    @Synchronized
    fun get(input: ScanResult): Wifi {
        Timber.v("Resolved Wifi: %s", input.BSSID)
        val id = input.BSSID + input.SSID
        var out: Wifi? = wifis.get(id)
        if (out == null) {
            out = Wifi(id)
            wifis.put(id, out)
        }
        out.bssid = input.BSSID
        out.ssid = input.SSID
        out.capabilities = input.capabilities
        val width = Width.find(input)
        val centerFrequency = centerFrequency(input, width)
        val band = Band.values().find { it.band.inRange(input.frequency) } ?: Band.GHZ2
        out.signal = Signal(input.frequency, centerFrequency, width, band, input.level, false)
        return out
    }

    @Synchronized
    fun get(input: WifiInfo): Wifi? {
        if (input.bssid == null) return null
        Timber.v("Resolved Wifi: %s", input.bssid)
        val ssid = WifiUtils.convertSsid(input.ssid)
        val id = input.bssid + ssid
        var out: Wifi? = wifis.get(id)
        if (out == null) {
            out = Wifi(id)
            wifis.put(id, out)
        }
        out.bssid = input.bssid
        out.ssid = ssid
        out.speed = input.linkSpeed
        //out.capabilities = input.capabilities
        //val width = Width.find(input)
        //val centerFrequency = centerFrequency(input, width)
        //val band = Band.values().find { it.band.inRange(input.frequency) } ?: Band.GHZ2
        //out.signal = Signal(input.frequency, centerFrequency, width, band, input.level, false)
        return out
    }

    @Throws
    @Synchronized
    private suspend fun updateCache(source: WifiDataSource, callback: () -> Unit) {
        if (wifis.isEmpty()) {
            source.gets(callback)?.let {
                if (it.isNotEmpty())
                    it.forEach { add(it) }
            }
        }
    }

    @Synchronized
    private fun sortedWifis(
        inputs: List<Wifi>
    ): List<Wifi> {
        val temp = ArrayList(inputs)
        val comparator = WifiComparator()
        temp.sortWith(comparator)
        return temp
    }

    private fun centerFrequency(input: ScanResult, width: Width): Int {
        try {
            var centerFrequency = centerFreq0(input)
            if (centerFrequency == 0) {
                centerFrequency = input.frequency
            } else if (isExtensionFrequency(input, width, centerFrequency)) {
                centerFrequency = (centerFrequency + input.frequency) / 2
            }
            return centerFrequency
        } catch (error: Throwable) {
            return input.frequency
        }
    }

    private fun isExtensionFrequency(
        input: ScanResult,
        width: Width,
        centerFrequency: Int
    ): Boolean {
        return width == Width.MHZ_40 && abs(input.frequency - centerFrequency) >= Width.MHZ_40.frequencyHalf
    }

    class WifiComparator : Comparator<Wifi> {
        override fun compare(left: Wifi, right: Wifi): Int {
            /*val leftLevel = left.signal?.level.value
            val rightLevel = right.signal?.level.value
            return rightLevel - leftLevel*/
            return (right.time - left.time).toInt()
        }
    }
}