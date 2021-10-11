package com.dreampany.word.data.model

import androidx.room.*
import com.dreampany.common.data.model.Base
import com.dreampany.common.misc.constant.Constant
import com.dreampany.common.misc.exts.currentMillis
import com.dreampany.word.misc.constant.Constants
import com.google.common.base.Objects
import kotlinx.parcelize.Parcelize

/**
 * Created by roman on 10/11/21
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
    primaryKeys = [Constant.Keys.ID],
    foreignKeys = [
        ForeignKey(
            entity = Word::class,
            parentColumns = [Constant.Keys.ID],
            childColumns = [Constants.Keys.Room.WORD_ID],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = Definition::class,
            parentColumns = [Constant.Keys.ID],
            childColumns = [Constants.Keys.Room.DEFINITION_ID],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class Example(
    override var id: String = Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Room.WORD_ID)
    var wordId: String = Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Room.DEFINITION_ID)
    var definitionId: String? = Constant.Default.NULL,
    var author: String? = Constant.Default.NULL,
    var title: String? = Constant.Default.NULL,
    var example: String? = Constant.Default.NULL,
    var url: String? = Constant.Default.NULL,
    var year: Long = Constant.Default.LONG,
    @ColumnInfo(name = Constant.Keys.CREATED_AT)
    var createdAt: Long = Constant.Default.LONG,
    @ColumnInfo(name = Constant.Keys.UPDATED_AT)
    var updatedAt: Long = Constant.Default.LONG
) : Base(id) {

    @Ignore
    constructor() : this(createdAt = currentMillis)

    constructor(id: String) : this(id = id, createdAt = currentMillis)

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Example
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Example: [example:$example]"
}