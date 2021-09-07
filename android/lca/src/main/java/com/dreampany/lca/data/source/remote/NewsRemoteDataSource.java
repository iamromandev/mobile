package com.dreampany.lca.data.source.remote;

import com.dreampany.lca.api.cc.model.CcNews;
import com.dreampany.lca.data.misc.NewsMapper;
import com.dreampany.lca.data.model.News;
import com.dreampany.lca.data.source.api.NewsDataSource;
import com.dreampany.lca.misc.CryptoCompareAnnote;
import com.dreampany.network.manager.NetworkManager;

import java.util.List;

import javax.inject.Singleton;


import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.functions.Function;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class NewsRemoteDataSource implements NewsDataSource {

    private final NetworkManager network;
    private final NewsMapper mapper;
    private final CryptoCompareNewsService service;

    public NewsRemoteDataSource(NetworkManager network,
                                NewsMapper mapper,
                                @CryptoCompareAnnote CryptoCompareNewsService service) {

        this.network = network;
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return Maybe.fromCallable(this::isEmpty);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return null;
    }

    @Override
    public boolean isExists(News news) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(News news) {
        return null;
    }

    @Override
    public long putItem(News news) {
        return 0;
    }

    @Override
    public Maybe<Long> putItemRx(News news) {
        return null;
    }

    @Override
    public List<Long> putItems(List<? extends News> news) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<? extends News> news) {
        return null;
    }

    @Override
    public int delete(News news) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(News news) {
        return null;
    }

    @Override
    public List<Long> delete(List<? extends News> news) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<? extends News> news) {
        return null;
    }

    @Override
    public News getItem(String id) {
        return null;
    }

    @Override
    public Maybe<News> getItemRx(String id) {
        return null;
    }

    @Override
    public List<News> getItems() {
        return null;
    }

    @Override
    public Maybe<List<News>> getItemsRx() {
        return null;
    }

    @Override
    public List<News> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<News>> getItemsRx(int limit) {
        return service.getNewsRx()
                .flatMap((Function<List<CcNews>, MaybeSource<List<News>>>) this::getItemsRx);
    }


    private Maybe<List<News>> getItemsRx(List<CcNews> items) {
        return Flowable.fromIterable(items)
                .map(mapper::toNews)
                .toList()
                .toMaybe();
    }
}
