package com.dreampany.network.nearby.data.model

import com.dreampany.network.misc.Constant
import com.google.common.base.Objects

/**
 * Created by roman on 7/2/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
data class Peer(
    val id: String,
    var raw: ByteArray = Constant.Default.BYTEARRAY
) {

    enum class State { LIVE, DEAD }

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Peer
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Peer[id:$id]"
}