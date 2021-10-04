package com.dreampany.common.data.model

import com.google.common.base.Objects

/**
 * Created by roman on 5/22/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class Base(
    @Transient open var id: String,
) : BaseParcel() {

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Base
        return Objects.equal(item.id, id)
    }

    override fun toString(): String = "Base[id:$id"
}