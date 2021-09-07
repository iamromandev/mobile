package com.dreampany.hello.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.currentMillis
import com.dreampany.hello.data.enums.Gender
import com.google.common.base.Objects
import com.google.firebase.database.ServerValue
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 25/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constant.Keys.ID],
        unique = true
    )],
    primaryKeys = [Constant.Keys.ID]
)
data class User(
    override var time: Long = Constant.Default.LONG,
    override var id: String = Constant.Default.STRING,
    var ref: String = Constant.Default.STRING,
    var timestamp: Map<String, String> = ServerValue.TIMESTAMP,
    var name: String? = Constant.Default.NULL,
    var photo: String? = Constant.Default.NULL,
    var birthday: Long = Constant.Default.LONG,
    var gender: Gender? = Constant.Default.NULL,
    @Embedded
    var country: Country? = Constant.Default.NULL,
    var phone: String? = Constant.Default.NULL,
    var status : String? = Constant.Default.NULL,
    var level : Int = Constant.Default.INT
) : Base() {

    @Ignore
    constructor() : this(time = currentMillis) {

    }

    constructor(id: String) : this(time = currentMillis, id = id) {

    }

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as User
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "User.id: $id"
}