package com.dreampany.word.data.model

import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.dreampany.framework.data.model.BaseParcelKt
import com.dreampany.word.misc.Constants
import com.google.common.base.Objects
import com.google.firebase.firestore.PropertyName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman-372 on 7/22/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Definition(
    @ColumnInfo(name = Constants.Word.PART_OF_SPEECH)
    @PropertyName(Constants.Word.PART_OF_SPEECH)
    private var partOfSpeech: String?,
    var text: String?
) : BaseParcelKt() {

    @Ignore
    constructor() : this("", "") {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Definition
        return Objects.equal(item.partOfSpeech, partOfSpeech) && Objects.equal(item.text, text)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(partOfSpeech, text)
    }

    @PropertyName(Constants.Word.PART_OF_SPEECH)
    fun setPartOfSpeech(partOfSpeech: String?) {
        this.partOfSpeech = partOfSpeech
    }

    @PropertyName(Constants.Word.PART_OF_SPEECH)
    fun getPartOfSpeech(): String? {
        return partOfSpeech
    }
}