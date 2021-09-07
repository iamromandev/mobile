package com.dreampany.hi.app

import com.dreampany.common.app.BaseApp
import com.dreampany.common.misc.exts.isDebug
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : BaseApp() {

    override fun onOpen() {
        initFirebase()
        initFresco()
    }

    override fun onClose() {

    }

    private fun initFirebase() {
        if (isDebug) return
        FirebaseApp.initializeApp(this)
    }

    private fun initFresco() {
        Fresco.initialize(
            this/*, ImagePipelineConfig.newBuilder(this)
                .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                .experiment()
                .setNativeCodeDisabled(true)
                .build()*/
        )
    }
}