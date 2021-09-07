package com.dreampany.translation.data.model

import com.dreampany.framework.data.model.Base

/**
 * Created by Roman-372 on 7/11/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class Translation: Base() {
    abstract var source: String
    abstract var target: String
}