package com.dreampany.hi.data.model

import androidx.room.*
import com.dreampany.common.data.enums.BaseEnum
import com.dreampany.common.data.model.Base
import com.dreampany.common.misc.constant.Constant
import com.dreampany.common.misc.exts.currentMillis
import com.dreampany.hi.misc.constant.Constants
import com.google.common.base.Objects
import kotlinx.parcelize.Parcelize


/**
 * Created by roman on 7/19/21
 * Copyright (c) 2021 butler. All rights reserved.
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
data class Message(
    override var id: String = Constant.Default.STRING,
    var author: String = Constant.Default.STRING,
    var type: Type = Type.TEXT,
    var text: String = Constant.Default.STRING,
    @Embedded(prefix = Constants.Keys.PREFIX_FILE)
    var file : File? = Constant.Default.NULL,
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
        val item = other as Message
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String =
        "Message [id:$id][createdAt:$createdAt][text:$text]"

    @Parcelize
    enum class Type : BaseEnum {
        TEXT;
        override val value: String get() = name
    }
}