package com.dreampany.pair.ui.model

import com.dreampany.common.data.enums.Action
import com.dreampany.common.data.enums.BaseType
import com.dreampany.common.data.enums.State
import com.dreampany.common.data.model.BaseParcel
import com.dreampany.common.data.model.Task
import com.dreampany.common.misc.constant.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 15/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class UiTask<T : BaseParcel, X : BaseType, Y: BaseType>(
    override var notify: Boolean = Constants.Default.BOOLEAN,
    override var fullscreen: Boolean = Constants.Default.BOOLEAN,
    override var collapseToolbar: Boolean = Constants.Default.BOOLEAN,
    override var type: X,
    override var subtype: Y,
    override var state: State = State.DEFAULT,
    override var action: Action = Action.DEFAULT,
    override var id: String? = Constants.Default.NULL,
    override var ids: List<String>? = Constants.Default.NULL,
    override var input: T? = Constants.Default.NULL,
    override var inputs: List<T>? = Constants.Default.NULL,
    override var extra: String? = Constants.Default.NULL,
    override var extras: List<String>? = Constants.Default.NULL
) : Task<T, X, Y>(
    notify,
    fullscreen,
    collapseToolbar,
    type,
    subtype,
    state,
    action,
    id,
    ids,
    input,
    inputs,
    extra,
    extras
)