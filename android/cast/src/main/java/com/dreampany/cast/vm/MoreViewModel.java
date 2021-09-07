package com.dreampany.cast.vm;

import android.app.Activity;
import android.app.Application;

import com.dreampany.cast.data.model.More;
import com.dreampany.cast.ui.enums.MoreType;
import com.dreampany.cast.ui.model.MoreItem;
import com.dreampany.cast.ui.model.UiTask;
import com.dreampany.framework.data.model.Response;
import com.dreampany.framework.misc.AppExecutors;
import com.dreampany.framework.misc.ResponseMapper;
import com.dreampany.framework.misc.RxMapper;
import com.dreampany.framework.util.SettingsUtil;
import com.dreampany.framework.ui.vm.BaseViewModel;
import com.dreampany.network.manager.NetworkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;

/**
 * Created by Hawladar Roman on 7/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class MoreViewModel extends BaseViewModel<More, MoreItem, UiTask<More>> {

    @Inject
    MoreViewModel(Application application,
                  RxMapper rx,
                  AppExecutors ex,
                  ResponseMapper rm,
                  NetworkManager network) {
        super(application, rx, ex, rm);
    }


    public void loads(boolean important) {
        if (!takeAction(important, getMultipleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItems())
                .doOnSubscribe(subscription -> postProgress(true))
                .subscribe(result -> postResult(Response.Type.ADD, result, true), this::postFailures);
        addMultipleSubscription(disposable);
    }

    private Maybe<List<MoreItem>> getItems() {
        return Maybe.fromCallable(() -> {
            List<MoreItem> items = new ArrayList<>();
            items.add(MoreItem.getItem(new More(MoreType.SETTINGS.name(), 0, MoreType.SETTINGS)));
            items.add(MoreItem.getItem(new More(MoreType.APPS.name(), 0, MoreType.APPS)));
            items.add(MoreItem.getItem(new More(MoreType.RATE_US.name(), 0 , MoreType.RATE_US)));
            items.add(MoreItem.getItem(new More(MoreType.FEEDBACK.name(), 0, MoreType.FEEDBACK)));
            //items.add(MoreItem.getItemBySymbolRx(new More(MoreType.INVITE)));
            items.add(MoreItem.getItem(new More(MoreType.LICENSE.name(), 0, MoreType.LICENSE)));
            items.add(MoreItem.getItem(new More(MoreType.ABOUT.name(), 0, MoreType.ABOUT)));
            return items;
        });
    }

    public void moreApps(Activity activity) {
        SettingsUtil.moreApps(Objects.requireNonNull(activity));
    }

    public void rateUs(Activity activity) {
        SettingsUtil.rateUs(activity);
    }

    public void sendFeedback(Activity activity) {
        SettingsUtil.feedback(activity);
    }
}
