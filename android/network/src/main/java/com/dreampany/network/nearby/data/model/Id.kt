package com.dreampany.network.nearby.data.model

import android.os.Parcelable
import com.dreampany.network.misc.Constant
import com.google.common.base.Objects
import kotlinx.parcelize.Parcelize


/**
 * Created by roman on 7/2/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Id(
    val id: String,
    val author: String,
    val target: String
) : Parcelable {

    override fun hashCode(): Int = Objects.hashCode(id, author, target)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Id
        return Objects.equal(this.id, item.id)
                && Objects.equal(this.author, item.author)
                && Objects.equal(this.target, item.target)
    }

    override fun toString(): String = "Id[id:$id][source:$author][target:$target]"

    companion object {
        val default: Id
            get() = Id(
                id = Constant.Default.STRING,
                author = Constant.Default.STRING,
                target = Constant.Default.STRING
            )
    }
}