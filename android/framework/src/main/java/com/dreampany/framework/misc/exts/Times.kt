package com.dreampany.framework.misc.exts

import android.text.format.DateUtils
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by roman on 3/22/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

private val UTC_PATTERN: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
private val SIMPLE_UTC_PATTERN: String = "yyyy-MM-dd'T'HH:mm:ss'Z'"

val currentMillis: Long get() = System.currentTimeMillis()
fun currentDay(): Int = Calendar.getInstance().dayOfMonth
fun currentMonth(): Int = Calendar.getInstance().month
fun currentYear(): Int = Calendar.getInstance().year

fun Calendar.format(pattern: String): String {
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    return format.format(this.time)
}

val String.utc: Long
    get() {
        val format = SimpleDateFormat(UTC_PATTERN, Locale.getDefault())
        try {
            return format.parse(this)?.time ?: 0L
        } catch (error: ParseException) {
            Timber.e(error)
            return 0L
        }
    }

val String.simpleUtc: Long
    get() {
        val format = SimpleDateFormat(SIMPLE_UTC_PATTERN, Locale.getDefault())
        try {
            return format.parse(this)?.time ?: 0L
        } catch (error: ParseException) {
            Timber.e(error)
            return 0L
        }
    }

fun String.utc(pattern : String): Long {
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    try {
        return format.parse(this)?.time ?: 0L
    } catch (error: ParseException) {
        Timber.e(error)
        return 0L
    }
}

val String.isoMillis: Long
    get() {
        val format = this.substring(2)
            .replace("H", ":")
            .replace("M", ":")
            .replace("S", "")
        var multiplier = 1L
        var duration = 0L
        format.reversed().split(":").forEach {
            duration += it.toLong() * multiplier
            multiplier *= 60
        }
        return duration
    }

val String.isoTime: String
    get() {
        val format = this.substring(2)
            .replace("H", ":")
            .replace("M", ":")
            .replace("S", "")

        return if (format.length <= 2) "0:$format" else format
    }

fun Long.format(pattern: String): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    return format.format(calendar.time)
}

fun String.calendar(pattern: String): Calendar? {
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    try {
        val date = format.parse(this) ?: return null
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date.time
        return calendar
    } catch (error: ParseException) {
    }
    return null
}

fun String.getDay(pattern: String): Int = calendar(pattern)?.dayOfMonth ?: 0

fun String.getMonth(pattern: String): Int = calendar(pattern)?.month ?: 0

val Long.time: String
    get() {
        val now = currentMillis
        var input = this
        if (now - input < 1000) {
            input = input + 1000
        }
        return DateUtils.getRelativeTimeSpanString(
            input,
            now,
            DateUtils.MINUTE_IN_MILLIS
        ).toString()
    }

val Long.millisToSeconds: Long
    get() = TimeUnit.MILLISECONDS.toSeconds(this)

val Long.secondsToMillis: Long
    get() = TimeUnit.SECONDS.toMillis(this)

val previousDay: Long
    get() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val time = calendar.timeInMillis
        calendar.clear()
        return time
    }

val previousWeek: Long
    get() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val time = calendar.timeInMillis
        calendar.clear()
        return time
    }

val previousMonth: Long
    get() = 1.previousMonth

val Int.previousMonth: Long
    get() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -this)
        val time = calendar.timeInMillis
        calendar.clear()
        return time
    }

val previousYear: Long
    get() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -1)
        val time = calendar.timeInMillis
        calendar.clear()
        return time
    }