package com.dreampany.network.nearby.data.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.network.misc.Constant
import com.google.common.base.Objects
import kotlinx.parcelize.Parcelize

/**
 * Created by roman on 7/31/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constant.Keys.ID, Constant.Keys.TYPE, Constant.Keys.SUBTYPE],
        unique = true
    )],
    primaryKeys = [Constant.Keys.ID, Constant.Keys.TYPE, Constant.Keys.SUBTYPE]
)
data class Packet(
    @Embedded
    val id: Id,
    val type: Byte = Constant.Default.BYTE,
    val subtype: Byte = Constant.Default.BYTE,
    var bytes : ByteArray = Constant.Default.BYTEARRAY
) : Parcelable {

    @Ignore
    constructor() : this(id = Id.default)

    override fun hashCode(): Int = Objects.hashCode(id, type, subtype)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Packet
        return Objects.equal(item.id, id) &&
                Objects.equal(item.type, type) &&
                Objects.equal(item.subtype, subtype)
    }

    override fun toString(): String = "Packet[id:$id][type:$id][subtype:$subtype]"

    fun hasProperty(type: String, subtype: String): Boolean {
        return (Objects.equal(type, this.type)
                && Objects.equal(subtype, this.subtype))
    }
}