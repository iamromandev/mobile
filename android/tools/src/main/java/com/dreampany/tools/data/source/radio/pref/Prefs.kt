package com.dreampany.tools.data.source.radio.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.Pref
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.currentMillis
import com.dreampany.tools.misc.constants.Constants
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton
import com.dreampany.tools.R
import com.dreampany.tools.data.model.radio.Page

/**
 * Created by roman on 15/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class Prefs
@Inject constructor(
    context: Context,
    private val gson: Gson
) : Pref(context) {

    override fun getPrivateName(context: Context): String = Constants.Keys.Pref.Radio.PREF

    val sort: String
        get() = getPublicly(
            context.getString(R.string.key_radio_settings_sort),
            context.getString(R.string.key_radio_settings_sort_value_name)
        )

    @Synchronized
    fun writePagesSelection() {
        setPrivately(Constants.Keys.Pref.Radio.PAGE, true)
    }

    val isPagesSelected: Boolean
        get() = getPrivately(Constants.Keys.Pref.Radio.PAGE, Constant.Default.BOOLEAN)

    @Synchronized
    fun writePages(inputs: List<Page>) {
        val json = gson.toJson(inputs)
        setPrivately(Constants.Keys.Pref.Radio.PAGES, json)
    }

    @Synchronized
    fun writePage(input: Page) {
        val pages = pages
        val inputs = if (pages.isNullOrEmpty()) arrayListOf<Page>() else ArrayList(pages)
        inputs.add(input)
        writePages(inputs)
    }

    val pages: List<Page>?
        get() {
            val json =
                getPrivately(Constants.Keys.Pref.Radio.PAGES, Constant.Default.NULL as String?)
            if (json.isNullOrEmpty()) {
                return null
            } else {
                return gson.fromJson(json, Array<Page>::class.java).toList()
            }
        }

    @Synchronized
    fun writeExpireTime(type: Page.Type) {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(type.value)
        }
        setPrivately(key.toString(), currentMillis)
    }

    @Synchronized
    fun writeExpireTime(type: Page.Type, order: String, offset: Long) {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(type.value)
            append(order)
            append(offset)
        }
        setPrivately(key.toString(), currentMillis)
    }

    @Synchronized
    fun writeExpireTime(
        type: Page.Type,
        countryCode: String,
        order: String,
        offset: Long
    ) {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(type.value)
            append(countryCode)
            append(order)
            append(offset)
        }
        setPrivately(key.toString(), currentMillis)
    }

    @Synchronized
    fun readExpireTime(type: Page.Type): Long {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(type.value)
        }
        return getPrivately(key.toString(), Constant.Default.LONG)
    }

    @Synchronized
    fun readExpireTime(
        type: Page.Type, order: String, offset: Long
    ): Long {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(type.value)
            append(order)
            append(offset)
        }
        return getPrivately(key.toString(), Constant.Default.LONG)
    }

    @Synchronized
    fun readExpireTime(
        type: Page.Type,
        order: String,
        countryCode: String,
        offset: Long
    ): Long {
        val key = StringBuilder(Constants.Keys.Pref.EXPIRE).apply {
            append(type.value)
            append(countryCode)
            append(order)
            append(offset)
        }
        return getPrivately(key.toString(), Constant.Default.LONG)
    }
}