package com.dreampany.pref.misc

import java.time.OffsetDateTime
import java.util.*

/**
 * Created by roman on 7/21/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
object Constant {
    object Default {
        val NULL = null
        const val BOOLEAN = false
        const val CHARACTER = 0.toChar()
        const val INT = 0
        const val LONG = 0L
        const val FLOAT = 0f
        const val DOUBLE = 0.0
        const val STRING = ""
        val TIMESTAMP = OffsetDateTime.MIN
        val LIST = Collections.emptyList<Any>()
    }
}

val Boolean?.value: Boolean get() = this ?: false

val Int?.value: Int get() = this ?: 0

val Long?.value: Long get() = this ?: 0L

val Float?.value: Float get() = this ?: 0.0f

val Double?.value: Double get() = this ?: 0.toDouble()

val String?.value: String get() = this ?: Constant.Default.STRING