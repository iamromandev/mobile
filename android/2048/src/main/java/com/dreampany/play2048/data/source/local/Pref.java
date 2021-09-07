package com.dreampany.play2048.data.source.local;

import android.content.Context;

import com.dreampany.framework.data.source.pref.FramePref;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Hawladar Roman on 3/7/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class Pref extends FramePref {
    
    @Inject
    Pref(Context context) {
        super(context);
    }
}