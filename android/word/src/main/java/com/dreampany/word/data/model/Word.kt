package com.dreampany.word.data.model

import androidx.room.*
import com.dreampany.common.data.model.Base
import com.dreampany.common.misc.constant.Constant
import com.dreampany.common.misc.exts.currentMillis
import com.dreampany.word.misc.constant.Constants
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
    primaryKeys = [Constant.Keys.ID],
    foreignKeys = [
        ForeignKey(
            entity = Language::class,
            parentColumns = [Constant.Keys.ID],
            childColumns = [Constants.Keys.Room.LANGUAGE_ID],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class Word(
    override var id: String = Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Room.LANGUAGE_ID)
    var languageId: String = Constant.Default.STRING,
    var word: String = Constant.Default.STRING,
    var origin: String? = Constant.Default.NULL,
    @ColumnInfo(name = Constant.Keys.CREATED_AT)
    var createdAt: Long = Constant.Default.LONG,
    @ColumnInfo(name = Constant.Keys.UPDATED_AT)
    var updatedAt: Long = Constant.Default.LONG,
    @Ignore
    var language: Language = Language(),
    @Ignore
    var pronunciations: MutableList<Pronunciation> = arrayListOf(),
    @Ignore
    var definitions: MutableList<Definition> = arrayListOf(),
    @Ignore
    var examples: MutableList<Example> = arrayListOf(),
    @Ignore
    var relations: MutableMap<RelationType, MutableList<Relation>> = mutableMapOf()
) : Base(id) {

    @Ignore
    constructor() : this(createdAt = currentMillis)

    constructor(id: String) : this(id = id, createdAt = currentMillis)

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Word
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Word: [id:$id] [word:$word]"
}