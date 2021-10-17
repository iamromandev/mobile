package com.dreampany.dictionary.ui.model

import com.dreampany.dictionary.data.model.Source
import com.dreampany.dictionary.data.model.Word
import com.google.common.base.Objects

/**
 * Created by roman on 10/17/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class SourcePageItem(
    val source: Source,
    val word: Word
) {
    override fun hashCode(): Int = source.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as SourcePageItem
        return Objects.equal(this.source.id, item.source.id)
    }
}