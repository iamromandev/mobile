package com.dreampany.tools.ui.wifi.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.framework.misc.exts.*
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.wifi.Strength
import com.dreampany.tools.data.model.wifi.Wifi
import com.dreampany.tools.databinding.WifiItemBinding
import com.google.common.base.Objects
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 12/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WifiItem
private constructor(
    val input: Wifi,
    var favorite: Boolean
) : ModelAbstractBindingItem<Wifi, WifiItemBinding>(input) {

    companion object {
        fun get(
            input: Wifi,
            favorite: Boolean = false
        ): WifiItem = WifiItem(input, favorite)
    }

    override fun hashCode(): Int = Objects.hashCode(input.id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as WifiItem
        return Objects.equal(this.input.id, item.input.id)
    }

    override var identifier: Long = hashCode().toLong()

    override val type: Int = R.id.adapter_wifi_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): WifiItemBinding =
        WifiItemBinding.inflate(inflater, parent, false)


    override fun bindView(bind: WifiItemBinding, payloads: List<Any>) {
        val strength = input.signal?.strength ?: Strength.ZERO
        val security = input.security

        bind.icon.setImageResource(strength.imageRes.value)
        bind.icon.setColorFilter(bind.color(strength.colorRes.value))

        bind.securityIcon.setImageResource(security.res)
        bind.securityIcon.setColorFilter(bind.color(strength.colorRes.value))

        bind.ssid.text =
            String.format(bind.string(R.string.format_ssid_bssid), input.ssid, input.bssid)
        bind.level.text =
            String.format(bind.string(R.string.format_wifi_level), input.signal?.level)
        bind.level.setTextColor(bind.color(strength.colorRes.value))

        bind.channel.text =
            bind.context.getString(R.string.format_wifi_channel, input.signal?.channelDisplay.value)
        bind.distance.text =
            bind.context.getString(R.string.format_wifi_distance, input.signal?.distance.value)

        bind.connection.visible(input.speed > 0)
    }

    override fun unbindView(binding: WifiItemBinding) {

    }
}