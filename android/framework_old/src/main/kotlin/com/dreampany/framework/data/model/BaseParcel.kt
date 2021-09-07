package com.dreampany.framework.data.model

import android.os.Parcelable

/**
 * Created by Roman-372 on 7/4/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseParcel : BaseSerial(), Parcelable {
    override fun describeContents() = 0
}