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
            entity = RelationType::class,
            parentColumns = [Constant.Keys.ID],
            childColumns = [Constants.Keys.Room.RELATION_TYPE_ID],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = Word::class,
            parentColumns = [Constant.Keys.ID],
            childColumns = [Constants.Keys.Room.LEFT_WORD_ID],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = Word::class,
            parentColumns = [Constant.Keys.ID],
            childColumns = [Constants.Keys.Room.RIGHT_WORD_ID],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class Relation(
    override var id: String = Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Room.RELATION_TYPE_ID)
    var relationTypeId: String = Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Room.LEFT_WORD_ID)
    var leftWordId: String = Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Room.RIGHT_WORD_ID)
    var rightWordId: String = Constant.Default.STRING,
    @ColumnInfo(name = Constant.Keys.CREATED_AT)
    var createdAt: Long = Constant.Default.LONG,
    @ColumnInfo(name = Constant.Keys.UPDATED_AT)
    var updatedAt: Long = Constant.Default.LONG,
    @Ignore
    var relationType: String = Constant.Default.STRING,
    @Ignore
    var leftWord: String = Constant.Default.STRING,
    @Ignore
    var rightWord: String = Constant.Default.STRING,
) : Base(id) {

    @Ignore
    constructor() : this(createdAt = currentMillis)

    constructor(id: String) : this(id = id, createdAt = currentMillis)

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Relation
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String =
        "Relation: [relationType:$relationType][leftWord:$leftWord][rightWord:$rightWord]"
}