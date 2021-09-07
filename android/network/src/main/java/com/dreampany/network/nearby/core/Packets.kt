package com.dreampany.network.nearby.core

import com.dreampany.network.misc.*
import com.dreampany.network.nearby.data.enums.Operation
import com.dreampany.network.nearby.data.enums.Subtype
import com.dreampany.network.nearby.data.enums.Type
import org.apache.commons.lang3.tuple.MutableTriple
import java.nio.ByteBuffer

/**
 * Created by roman on 7/9/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */

val ByteArray.broadcastDataPacket: ByteArray
    get() {
        val buf = ByteBuffer.allocate(1 + 1 + 1 + size)
        buf.put(Operation.BROADCAST.value)
        buf.put(Type.DATA.value)
        buf.put(Subtype.RAW.value)
        buf.put(this)
        return buf.array()
    }


val MutableTriple<String, Type, Subtype>.requestPacket: ByteArray?
    get() {
        if (middle == Type.PEER) {
            //if (right == Subtype.ID) return Packets.requestPeerIdPacket
            //if (right == Subtype.HASH) return Packets.requestPeerHashPacket
            if (right == Subtype.RAW) return Packets.requestPeerRawPacket
        }
        return null
    }

val String.replyPeerIdPacket: ByteArray
    get() {
        val peerIdBytes = toByteArray()
        val buf = ByteBuffer.allocate(1 + 1 + 1 + peerIdBytes.size)
        buf.put(Operation.REPLY.value)
        buf.put(Type.PEER.value)
        buf.put(Subtype.ID.value)
        buf.put(peerIdBytes)
        return buf.array()
    }

val ByteArray?.responsePeerHashPacket: ByteArray
    get() {
        val buf = ByteBuffer.allocate(1 + 1 + 1 + 8)
        buf.put(Operation.REPLY.value)
        buf.put(Type.PEER.value)
        buf.put(Subtype.HASH.value)
        buf.putLong(this.hash256Long)
        return buf.array()
    }

val ByteArray?.replyPeerRawPacket: ByteArray
    get() {
        val buf = ByteBuffer.allocate(1 + 1 + 1 + length)
        buf.put(Operation.REPLY.value)
        buf.put(Type.PEER.value)
        buf.put(Subtype.RAW.value)
        this?.let { buf.put(this) }
        return buf.array()
    }

fun ByteArray.requestDataPacket(id: Long): ByteArray {
    val buf = ByteBuffer.allocate(1 + 1 + 1 + 8 + size)
    buf.put(Operation.REQUEST.value)
    buf.put(Type.DATA.value)
    buf.put(Subtype.RAW.value)
    buf.putLong(id)
    buf.put(this)
    return buf.array()
}

val Long.replyDataRawPacket: ByteArray
    get() {
        val buf = ByteBuffer.allocate(1 + 1 + 1 + 8)
        buf.put(Operation.REPLY.value)
        buf.put(Type.DATA.value)
        buf.put(Subtype.RAW.value)
        buf.putLong(this)
        return buf.array()
    }

val ByteArray.isBroadcast: Boolean
    get() {
        val type = firstOrNull()
        return type == Operation.BROADCAST.value
    }

val ByteArray.isRequest: Boolean
    get() {
        val type = firstOrNull()
        return type == Operation.REQUEST.value
    }

val ByteArray.isReply: Boolean
    get() {
        val type = firstOrNull()
        return type == Operation.REPLY.value
    }

val ByteArray.isPeer: Boolean
    get() {
        val type = secondOrNull()
        return type == Type.PEER.value
    }

val ByteArray.isData: Boolean
    get() {
        val type = secondOrNull()
        return type == Type.DATA.value
    }

val ByteArray.isFile: Boolean
    get() {
        val type = secondOrNull()
        return type == Type.FILE.value
    }

val ByteArray.isId: Boolean
    get() {
        val subtype = thirdOrNull()
        return subtype == Subtype.ID.value
    }

val ByteArray.isHash: Boolean
    get() {
        val subtype = thirdOrNull()
        return subtype == Subtype.HASH.value
    }

val ByteArray.isIdHash: Boolean
    get() {
        val subtype = thirdOrNull()
        return subtype == Subtype.ID_HASH.value
    }

val ByteArray.isRaw: Boolean
    get() {
        val subtype = thirdOrNull()
        return subtype == Subtype.RAW.value
    }

val ByteArray.peerIdHash: Pair<String, Long>
    get() {
        val buf = Packets.copyToBuffer(this, 3)
        val peerHash = buf.long
        val peerIdBytes = buf.remaining
        return Pair(peerIdBytes.string, peerHash)
    }

val ByteArray.peerId: String
    get() {
        val peerIdBytes = Packets.copy(this, 3)
        return peerIdBytes.string
    }

val ByteArray.peerHash: Long
    get() {
        val buf = Packets.copyToBuffer(this, 3)
        return buf.long
    }

val ByteArray.peerRaw: ByteArray get() = Packets.copy(this, 3)

val ByteArray.dataRaw: Pair<Long, ByteArray>
    get() {
        val buf = Packets.copyToBuffer(this, 3)
        val id = buf.long
        val packet = buf.remaining
        return Pair(id, packet)
    }

class Packets {
    companion object {

        fun broadcastPeerIdHashPacket(peerId: String, peerHash: Long): ByteArray {
            val peerIdBytes = peerId.toByteArray()
            val buf = ByteBuffer.allocate(1 + 1 + 1 + 8 + peerIdBytes.size)
            buf.put(Operation.BROADCAST.value)
            buf.put(Type.PEER.value)
            buf.put(Subtype.ID_HASH.value)
            buf.putLong(peerHash)
            buf.put(peerIdBytes)
            return buf.array()
        }

        val requestPeerIdPacket: ByteArray = ByteBuffer.allocate(1 + 1 + 1)
            .put(Operation.REQUEST.value)
            .put(Type.PEER.value)
            .put(Subtype.ID.value)
            .array()

        val requestPeerHashPacket: ByteArray = ByteBuffer.allocate(1 + 1 + 1)
            .put(Operation.REQUEST.value)
            .put(Type.PEER.value)
            .put(Subtype.HASH.value)
            .array()

        val requestPeerRawPacket: ByteArray = ByteBuffer.allocate(1 + 1 + 1)
            .put(Operation.REQUEST.value)
            .put(Type.PEER.value)
            .put(Subtype.RAW.value)
            .array()

/*        val Long.peerHashPacket: ByteArray
            get() {
                val buf = ByteBuffer.allocate(1 + 1 + 8)
                buf.put(TYPE_PEER)
                buf.put(SUBTYPE_HASH)
                buf.putLong(this)
                return buf.array()
            }*/

/*        val ByteArray?.peerMetaPacket: ByteArray
            get() {
                val buf = ByteBuffer.allocate(1 + 1 + length)
                buf.put(TYPE_PEER)
                buf.put(SUBTYPE_META)
                this?.let { buf.put(this) }
                return buf.array()
            }

        */


        fun copy(src: ByteArray, from: Int): ByteArray = src.copyOfRange(from, src.size)
        fun copyToBuffer(src: ByteArray, from: Int): ByteBuffer = ByteBuffer.wrap(copy(src, from))
    }
}