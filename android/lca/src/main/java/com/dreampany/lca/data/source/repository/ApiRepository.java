package com.dreampany.lca.data.source.repository;

import com.annimon.stream.Stream;
import com.dreampany.framework.data.misc.StateMapper;
import com.dreampany.framework.data.model.State;
import com.dreampany.framework.data.source.repository.StateRepository;
import com.dreampany.framework.misc.ResponseMapper;
import com.dreampany.framework.misc.RxMapper;
import com.dreampany.framework.util.DataUtil;
import com.dreampany.framework.util.TimeUtil;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.enums.ItemState;
import com.dreampany.lca.data.enums.ItemSubtype;
import com.dreampany.lca.data.enums.ItemType;
import com.dreampany.lca.data.misc.CoinAlertMapper;
import com.dreampany.lca.data.misc.CoinMapper;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.CoinAlert;
import com.dreampany.lca.data.enums.Currency;
import com.dreampany.lca.data.source.pref.Pref;
import com.google.common.collect.Maps;

import io.reactivex.Maybe;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Roman-372 on 2/12/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@Singleton
public class ApiRepository {

    private final RxMapper rx;
    private final ResponseMapper rm;
    private final Pref pref;
    private final CoinMapper coinMapper;
    private final StateMapper stateMapper;
    private final CoinAlertMapper alertMapper;
    private final CoinRepository coinRepo;
    private final StateRepository stateRepo;
    private final CoinAlertRepository alertRepo;
    private final Map<Coin, Boolean> favorites;
    private final Map<Coin, Boolean> alerts;

    @Inject
    ApiRepository(RxMapper rx,
                  ResponseMapper rm,
                  Pref pref,
                  CoinMapper coinMapper,
                  StateMapper stateMapper,
                  CoinAlertMapper alertMapper,
                  CoinRepository coinRepo,
                  StateRepository stateRepo,
                  CoinAlertRepository alertRepo) {
        this.rx = rx;
        this.rm = rm;
        this.pref = pref;
        this.coinMapper = coinMapper;
        this.stateMapper = stateMapper;
        this.alertMapper = alertMapper;
        this.coinRepo = coinRepo;
        this.stateRepo = stateRepo;
        this.alertRepo = alertRepo;
        favorites = Maps.newConcurrentMap();
        alerts = Maps.newConcurrentMap();
    }

    public boolean hasState(Coin coin, ItemSubtype subtype, ItemState state) {
        boolean stated = stateRepo.getCountById(coin.getId(), ItemType.COIN.name(), subtype.name(), state.name()) > 0;
        return stated;
    }

    public long putState(Coin coin, ItemSubtype subtype, ItemState state) {
        State s = new State(coin.getId(), ItemType.COIN.name(), subtype.name(), state.name());
        s.setTime(TimeUtil.currentTime());
        long result = stateRepo.putItem(s);
        return result;
    }

    public int removeState(Coin coin, ItemSubtype subtype, ItemState state) {
        State s = new State(coin.getId(), ItemType.COIN.name(), subtype.name(), state.name());
        s.setTime(TimeUtil.currentTime());
        int result = stateRepo.delete(s);
        return result;
    }

    public long putFavorite(Coin coin) {
        long result = putState(coin, ItemSubtype.DEFAULT, ItemState.FAVORITE);
        return result;
    }

    public int removeFavorite(Coin coin) {
        int result = removeState(coin, ItemSubtype.DEFAULT, ItemState.FAVORITE);
        return result;
    }

    public boolean isFavorite(Coin coin) {
        if (!favorites.containsKey(coin)) {
            boolean favorite = hasState(coin, ItemSubtype.DEFAULT, ItemState.FAVORITE);
            favorites.put(coin, favorite);
        }
        return favorites.get(coin);
    }

    public boolean hasAlert(Coin coin) {
        if (!alerts.containsKey(coin)) {
            boolean alert = alertRepo.isExists(coin.getId());
            alerts.put(coin, alert);
        }
        return alerts.get(coin);
    }

    public boolean toggleFavorite(Coin coin) {
        boolean favorite = hasState(coin, ItemSubtype.DEFAULT, ItemState.FAVORITE);
        if (favorite) {
            removeFavorite(coin);
            favorites.put(coin, false);
        } else {
            putFavorite(coin);
            favorites.put(coin, true);
        }
        return favorites.get(coin);
    }

    public Maybe<List<Coin>> getItemsIfRx(CoinSource source, Currency currency, int index, int limit) {
        return coinRepo.getItemsRx(source, currency, index, limit);
    }

    public List<Coin> getItemsIf(CoinSource source, Currency currency, List<String> coinIds) {
        return coinRepo.getItemsRx(source, currency, coinIds).blockingGet();
    }

    public Coin getItemIf(CoinSource source, Currency currency, String coinId) {
        return getItemIfRx(source, currency, coinId).blockingGet();
    }

    public Maybe<Coin> getItemIfRx(CoinSource source, Currency currency, String coinId) {
        return coinRepo.getItemRx(source, currency, coinId);
    }

    public List<Coin> getFavorites(CoinSource source, Currency currency) {
        List<State> states = stateRepo.getItems(ItemType.COIN.name(), ItemSubtype.DEFAULT.name(), ItemState.FAVORITE.name());
        return getItemsOfStatesIf(source, currency, states);
    }

    public CoinAlert getCoinAlert(String coinId) {
        return alertRepo.getItem(coinId);
    }

    public Maybe<List<CoinAlert>> getCoinAlertsRx() {
        return alertRepo.getItemsRx();
    }

    public long putItem(Coin coin, CoinAlert coinAlert) {
        long result = alertRepo.putItem(coinAlert);
        if (result != -1) {
            alerts.put(coin, true);
        }
        return result;
    }

    public int delete(Coin coin, CoinAlert coinAlert) {
        int result = alertRepo.delete(coinAlert);
        if (result != -1) {
            alerts.put(coin, false);
        }
        return result;
    }

    public int getCoinCount() {
        return coinRepo.getCount();
    }

    private List<Coin> getItemsOfStatesIf(CoinSource source, Currency currency, List<State> states) {
        if (DataUtil.isEmpty(states)) {
            return null;
        }
        List<Coin> result = new ArrayList<>(states.size());
        Stream.of(states).forEach(state -> {
            Coin item = coinMapper.toItem(source, currency, state, coinRepo);
            if (item != null) {
                result.add(item);
            }
        });
        return result;
    }
}
