package com.dreampany.lca.data.source.repository;

import com.dreampany.framework.data.source.repository.Repository;
import com.dreampany.framework.injector.annote.Remote;
import com.dreampany.framework.misc.ResponseMapper;
import com.dreampany.framework.injector.annote.Room;
import com.dreampany.framework.misc.RxMapper;
import com.dreampany.lca.data.model.Exchange;
import com.dreampany.lca.data.source.api.ExchangeDataSource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;


import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 6/26/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class ExchangeRepository extends Repository<Long, Exchange> implements ExchangeDataSource {

    private final ExchangeDataSource local;
    private final ExchangeDataSource remote;


    @Inject
    ExchangeRepository(RxMapper rx,
                       ResponseMapper rm,
                       @Room ExchangeDataSource local,
                       @Remote ExchangeDataSource remote) {
        super(rx, rm);
        this.local = local;
        this.remote = remote;
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return Maybe.fromCallable(this::isEmpty);
    }

    @Override
    public int getCount() {
        return local.getCount();
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return null;
    }

    @Override
    public List<Exchange> getItems(String symbol, int limit) {
        return null;
    }

    @Override
    public Maybe<List<Exchange>> getItemsRx(String symbol, int limit) {
        return getWithSave(remote.getItemsRx(symbol, limit));
    }

    @Override
    public boolean isExists(Exchange exchange) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Exchange exchange) {
        return null;
    }

    @Override
    public long putItem(Exchange exchange) {
        return local.putItem(exchange);
    }

    @Override
    public Maybe<Long> putItemRx(Exchange exchange) {
        return Maybe.fromCallable(() -> putItem(exchange));
    }

    @Override
    public List<Long> putItems(List<? extends Exchange> exchanges) {
        return local.putItems(exchanges);
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<? extends Exchange> exchanges) {
        return Maybe.fromCallable(() -> putItems(exchanges));
    }

    @Override
    public int delete(Exchange exchange) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(Exchange exchange) {
        return null;
    }

    @Override
    public List<Long> delete(List<? extends Exchange> exchanges) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<? extends Exchange> exchanges) {
        return null;
    }

    @Override
    public Exchange getItem(String id) {
        return null;
    }

    @Override
    public Maybe<Exchange> getItemRx(String id) {
        return null;
    }

    @Override
    public List<Exchange> getItems() {
        return null;
    }

    @Override
    public Maybe<List<Exchange>> getItemsRx() {
        return null;
    }

    @Override
    public List<Exchange> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Exchange>> getItemsRx(int limit) {
        return null;
    }

    private Maybe<List<Exchange>> getWithSave(Maybe<List<Exchange>> source) {
        return source
                .onErrorReturnItem(new ArrayList<>())
                .filter(items -> !(items == null || items.isEmpty()))
                .doOnSuccess(items -> {
                    rx.compute(putItemsRx(items)).subscribe();
                });
    }
}
