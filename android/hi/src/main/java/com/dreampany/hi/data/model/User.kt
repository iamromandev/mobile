package com.dreampany.hi.data.model

import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.dreampany.common.data.model.Base
import com.dreampany.common.misc.constant.Constant
import com.dreampany.common.misc.exts.currentMillis
import com.dreampany.hi.data.enums.Gender
import com.google.common.base.Objects
import kotlinx.parcelize.Parcelize

/**
 * Created by roman on 7/10/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Parcelize
data class User(
    override var id: String = Constant.Default.STRING,
    var name: String? = Constant.Default.NULL,
    var birthday: Long = Constant.Default.LONG,
    var gender: Gender? = Constant.Default.NULL,
    var phone: String? = Constant.Default.NULL,
    var status: String? = Constant.Default.NULL,
    var level: Int = Constant.Default.INT,
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
        val item = other as User
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "User [id:$id][createdAt:$createdAt]"
}