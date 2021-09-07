package com.dreampany.framework.misc.extensions

import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.ImageRequestBuilder

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun SimpleDraweeView?.setUrl(url: String?): SimpleDraweeView? {
    if (this == null) return this
    val uri = url.toUri() ?: return this
    val request =
        ImageRequestBuilder.newBuilderWithSource(uri)
            //.setResizeOptions(new ResizeOptions(width, height))
            .build()
    setController(
        Fresco.newDraweeControllerBuilder()
            .setOldController(getController())
            .setImageRequest(request)
            //.setTapToRetryEnabled(retryEnabled)
            .build()
    )
    return this
}