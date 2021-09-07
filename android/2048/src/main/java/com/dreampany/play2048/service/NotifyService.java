package com.dreampany.play2048.service;

import androidx.annotation.NonNull;

import com.dreampany.play2048.vm.NotifyViewModel;
import com.dreampany.framework.api.service.BaseJobService;
import com.firebase.jobdispatcher.JobParameters;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 7/22/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class NotifyService extends BaseJobService {

    @Inject
    NotifyViewModel vm;
    @Override
    protected boolean doJob(@NonNull JobParameters job) {
        vm.notifyIf();
        return true;
    }

    @Override
    protected boolean done(@NonNull JobParameters job) {
        vm.clear();
        return true;
    }
}