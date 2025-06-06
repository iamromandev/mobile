package com.dreampany.hi.misc.exts

import androidx.annotation.DrawableRes
import androidx.core.net.toUri
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.ImageRequestBuilder

/**
 * Created by roman on 7/18/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
fun SimpleDraweeView.setUrl(url: String?): SimpleDraweeView {
    val uri = url?.toUri() ?: return this
    val request =
        ImageRequestBuilder.newBuilderWithSource(uri)
            //.setResizeOptions(new ResizeOptions(width, height))
            .build()
    this.setController(
        Fresco.newDraweeControllerBuilder()
            .setOldController(getController())
            .setImageRequest(request)
            //.setTapToRetryEnabled(retryEnabled)
            .build()
    )
    return this
}

/*fun SimpleDraweeView.setRes(@DrawableRes resId: Int): SimpleDraweeView {
    val request =
        ImageRequestBuilder.newBuilderWithResourceId(resId)
            //.setResizeOptions(new ResizeOptions(width, height))
            .build()
    this.setController(
        Fresco.newDraweeControllerBuilder()
            .setOldController(getController())
            .setImageRequest(request)
            .build()
    )
    return this
}*/

fun SimpleDraweeView.setRes(@DrawableRes resId: Int): SimpleDraweeView {
    this.setImageResource(resId)
    return this
}
