package com.dreampany.hi.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.dreampany.common.data.model.Base
import com.dreampany.common.misc.constant.Constant
import com.dreampany.common.misc.exts.currentMillis
import com.google.common.base.Objects
import kotlinx.parcelize.Parcelize


/**
 * Created by roman on 5/14/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Auth(
    override var id: String = Constant.Default.STRING,
    var type: Type = Type.EMAIL,
    var username: String? = Constant.Default.NULL,
    var email: String? = Constant.Default.NULL,
    var password: String? = Constant.Default.NULL,
    var registered: Boolean = Constant.Default.BOOLEAN,
    var verified: Boolean = Constant.Default.BOOLEAN,
    var logged: Boolean = Constant.Default.BOOLEAN,
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
        val item = other as Auth
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Auth [id:$id][createdAt:$createdAt]"

    @Parcelize
    enum class Type : Parcelable {
        EMAIL,
        GOOGLE,
        FACEBOOK;

        val value: String
            get() = name

        val isSocial: Boolean
            get() = this == GOOGLE || this == FACEBOOK
    }
}