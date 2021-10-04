package com.dreampany.word.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.common.data.model.Base
import com.dreampany.common.misc.constant.Constant
import com.dreampany.common.misc.exts.currentMillis
import com.google.common.base.Objects
import kotlinx.parcelize.Parcelize

/**
 * Created by roman on 10/3/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constant.Keys.ID],
        unique = true
    )],
    primaryKeys = [Constant.Keys.ID]
)
data class Language(
    override var id: String = Constant.Default.STRING,
    var code: String = Constant.Default.STRING,
    var name: String = Constant.Default.STRING,
    @ColumnInfo(name = Constant.Keys.CREATED_AT)
    var createdAt: Long = Constant.Default.LONG,
    @ColumnInfo(name = Constant.Keys.UPDATED_AT)
    var updatedAt: Long = Constant.Default.LONG
) : Base(id) {

    @Ignore
    constructor() : this(id = Constant.Default.STRING)

    constructor(id: String) : this(id = id, createdAt = currentMillis)

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Language
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Language [code:$code][name:$name]"
}