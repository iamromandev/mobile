package com.dreampany.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import io.reactivex.CompletableEmitter;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 5/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class RxCompletableHandler<T> implements OnSuccessListener<T>, OnFailureListener, OnCompleteListener<T> {

    private final CompletableEmitter emitter;

    private RxCompletableHandler(CompletableEmitter emitter) {
        this.emitter = emitter;
    }

    public static <T> void assignOnTask(CompletableEmitter emitter, Task<T> task) {
        RxCompletableHandler<T> handler = new RxCompletableHandler<>(emitter);
        task.addOnFailureListener(handler);
        task.addOnSuccessListener(handler);
        try {
            task.addOnCompleteListener(handler);
        } catch (Throwable error) {
            Timber.e(error);
        }
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (emitter.isDisposed()) {
            return;
        }
        emitter.onComplete();
        Timber.v("Completed");
    }

    @Override
    public void onFailure(@NonNull Exception error) {
        if (emitter.isDisposed()) {
            return;
        }
        emitter.onError(error);
        Timber.e("Error %s", error.getMessage());
    }

    @Override
    public void onSuccess(Object result) {
        if (emitter.isDisposed()) {
            return;
        }
        emitter.onComplete();
        Timber.v("Completed successfully");
    }
}
