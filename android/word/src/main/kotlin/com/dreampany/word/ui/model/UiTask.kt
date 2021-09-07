package com.dreampany.word.ui.model

import com.dreampany.framework.data.model.BaseKt
import com.dreampany.framework.data.model.Task
import com.dreampany.word.ui.enums.UiSubtype
import com.dreampany.word.ui.enums.UiType
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-07-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class UiTask<T : BaseKt>(
    val fullscreen: Boolean,
    val type: UiType,
    val subtype: UiSubtype,
    override var input: T?,
    override var comment: String?
) : Task<T>(input, comment) {
}