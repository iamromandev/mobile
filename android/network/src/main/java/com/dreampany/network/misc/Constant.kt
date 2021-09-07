package com.dreampany.network.misc

import android.content.Context
import java.time.OffsetDateTime
import java.util.*

/**
 * Created by roman on 7/31/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class Constant {

    companion object {
        fun lastAppId(context: Context?): String = context.lastApplicationId

        fun database(context: Context?, type: String): String {
            return lastAppId(context).lastPart(Sep.DOT).plus(Sep.DOT).plus(type).plus(Sep.DOT)
                .plus(Room.POST_FIX_DB)
        }
    }

    object Default {
        const val BOOLEAN: Boolean = false
        const val CHARACTER: Char = 0.toChar()
        const val BYTE: Byte = 0
        const val INT: Int = 0
        const val LONG: Long = 0L
        const val FLOAT: Float = 0f
        const val DOUBLE: Double = 0.0
        const val STRING: String = ""
        val BYTEARRAY: ByteArray = ByteArray(0)
        val NULL = null
        val TIMESTAMP: OffsetDateTime = OffsetDateTime.MIN
        val LIST: List<Any> = Collections.emptyList<Any>()
    }

    object Sep {
        const val DOT = '.'
    }

    object Keys {
        const val ID = "id"
        const val TYPE = "type"
        const val SUBTYPE = "subtype"
    }

    object Room {
        const val TYPE_NEARBY = "nearby"
        const val POST_FIX_DB = "db"
    }
}