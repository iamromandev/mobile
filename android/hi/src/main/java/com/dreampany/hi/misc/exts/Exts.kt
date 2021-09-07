package com.dreampany.hi.misc.exts

import java.nio.ByteBuffer


/**
 * Created by roman on 7/18/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
private const val MESSAGE_TYPE_TEXT_MESSAGE: Byte = 1

val ByteArray.isTextMessage: Boolean
    get() {
        val type = firstOrNull()
        return type == MESSAGE_TYPE_TEXT_MESSAGE
    }

val ByteArray.textMessagePacket: ByteArray
    get() {
        val buf = ByteBuffer.allocate(1 + size)
        buf.put(MESSAGE_TYPE_TEXT_MESSAGE)
        buf.put(this)
        return buf.array()
    }