package com.dreampany.hi.data.model

import androidx.room.*
import com.dreampany.common.data.model.Base
import com.dreampany.common.misc.constant.Constant
import com.dreampany.hi.misc.constant.Constants
import com.google.common.base.Objects
import kotlinx.parcelize.Parcelize

/**
 * Created by roman on 8/27/21
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
data class File(
    override var ref: String = Constant.Default.STRING,
    override var id: String = Constant.Default.STRING,
    @ColumnInfo(name = Constant.Keys.CREATED_AT)
    override var createdAt: Long = Constant.Default.LONG,
    @ColumnInfo(name = Constant.Keys.UPDATED_AT)
    override var updatedAt: Long = Constant.Default.LONG,
    var uri: String = Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.THUMB_URI)
    var thumbUri: String = Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.MIME_TYPE)
    var mimeType: String = Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.DISPLAY_NAME)
    var displayName: String = Constant.Default.STRING,
    var title: String = Constant.Default.STRING,
    var size: Long = Constant.Default.LONG,
    @Embedded
    var coordinate: Coordinate? = Constant.Default.NULL
) : Base(ref, id, createdAt, updatedAt) {

    @Ignore
    constructor() : this(ref = Constant.Default.STRING)

    constructor(id: String) : this(ref = Constant.Default.STRING, id = id)

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Message
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "File[uri:$uri][displayName:$displayName]"

}