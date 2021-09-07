/*
package com.dreampany.network.manager;

import android.content.Context;

import androidx.annotation.Nullable;

import com.dreampany.network.api.BluetoothApi;
import com.dreampany.network.api.InternetApi;
import com.dreampany.network.api.MobileApi;
import com.dreampany.network.api.WifiApApi;
import com.dreampany.network.api.WifiApi;
import com.dreampany.network.data.model.Network;
import com.dreampany.network.misc.RxMapper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

*/
/**
 * Created by Hawladar Roman on 8/18/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 *//*

@Singleton
public final class NetworkManager {

    public interface Callback {
        void onResult(Network... networks);
    }

    private final Context context;
    private final RxMapper rx;
*/
/*    private final WifiApi wifi;
    private final WifiApApi ap;
    private final MobileApi mobile;
    private final BluetoothApi bt;
    private final InternetApi internetApi;*//*

    private volatile boolean internet;
    private final Set<Callback> callbacks;
    private final Map<Callback, Boolean> checkInternets;

    private boolean resultFired;

    @Inject
    NetworkManager(Context context,
                   RxMapper rx,
                   WifiApi wifi,
                   WifiApApi ap,
                   MobileApi mobile,
                   BluetoothApi bt,
                   InternetApi internetApi) {
        this.context = context.getApplicationContext();
        this.rx = rx;
        this.wifi = wifi;
        this.ap = ap;
        this.mobile = mobile;
        this.bt = bt;
        this.internetApi = internetApi;
        callbacks = Sets.newConcurrentHashSet();
        checkInternets = Maps.newConcurrentMap();

        resultFired = false;
    }

    void onResult(boolean internet) {
        resultFired = true;
        this.internet = internet;
        postActiveNetworks();
    }

    public void observe(Callback callback, boolean checkInternet) {
        callbacks.add(callback);
        checkInternets.put(callback, checkInternet);
        startInternetIfPossible();
    }

    public void deObserve(Callback callback, boolean stopInternetCheck) {
        callbacks.remove(callback);
        if (stopInternetCheck) {
            checkInternets.remove(callback);
        }
        stopInternetIfPossible();
    }

    public boolean isObserving() {
        return resultFired && !callbacks.isEmpty();
    }

    public List<Network> getNetworks() {
        List<Network> networks = new ArrayList<>();
        networks.add(wifi.getNetwork(internet));
        networks.add(ap.getNetwork(internet));
        networks.add(bt.getNetwork(internet));
        return networks;
    }

    public List<Network> getActiveNetworks() {
        List<Network> networks = new ArrayList<>();
        if (wifi.isEnabled()) {
            networks.add(wifi.getNetwork(internet));
        }
        if (ap.isEnabled()) {
            networks.add(ap.getNetwork(internet));
        }
        if (mobile.isEnabled()) {
            networks.add(mobile.getNetwork(internet));
        }
*/
/*        if (bt.isEnabled()) {
            networks.add(bt.getNetwork(internet));
        }*//*

        return networks;
    }

    public boolean hasInternet() {
        return internet;
    }

    private void startInternetIfPossible() {
        for (Map.Entry<Callback, Boolean> entry : checkInternets.entrySet()) {
            if (entry.getValue()) {
                internetApi.start(this::onResult);
                break;
            }
        }
    }

    private void stopInternetIfPossible() {
        boolean checkInternet = false;
        for (Map.Entry<Callback, Boolean> entry : checkInternets.entrySet()) {
            if (entry.getValue()) {
                checkInternet = true;
                break;
            }
        }
        if (!checkInternet) {
            internetApi.stop(this::onResult);
        }
    }

    private void postActiveNetworks() {
        List<Network> result = getActiveNetworks();
        Network[] networks = result.toArray(new Network[0]);
        for (Callback callback : callbacks) {
            callback.onResult(networks);
        }
    }
}
*/
