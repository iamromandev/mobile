/*
package com.dreampany.frame.data.source.repository;

import com.dreampany.frame.data.model.Store;
import com.dreampany.frame.data.source.api.StoreDataSource;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.Room;
import com.dreampany.frame.misc.RxMapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;

*/
/**
 * Created by Hawladar Roman on 7/18/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 *//*

@Singleton
public class StoreRepository extends Repository<String, Store> implements StoreDataSource {

    private final StoreDataSource room;

    @Inject
    StoreRepository(RxMapper rx,
                    ResponseMapper rm,
                    @Room StoreDataSource room) {
        super(rx, rm);
        this.room = room;
    }

    @Override
    public List<String> getItemsOf(String type, String subtype, String state) {
        return room.getItemsOf(type, subtype, state);
    }

    @Override
    public Maybe<List<String>> getItemsOfRx(String type, String subtype, String state) {
        return room.getItemsOfRx(type, subtype, state);
    }

    @Override
    public Maybe<List<String>> getItemsOfRx(String type, String subtype, String state, int limit) {
        return room.getItemsOfRx(type, subtype, state, limit);
    }

    @Override
    public List<Store> getItems(String type, String subtype, String state) {
        return null;
    }

    @Override
    public Maybe<List<Store>> getItemsRx(String type, String subtype, String state) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return room.isEmpty();
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return Maybe.fromCallable(this::isEmpty);
    }

    @Override
    public int getCount() {
        return room.getCount();
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return room.getCountRx();
    }

    @Override
    public boolean isExists(Store store) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Store store) {
        return null;
    }

    @Override
    public long putItem(Store store) {
        return room.putItem(store);
    }

    @Override
    public Maybe<Long> putItemRx(Store store) {
        return null;
    }

    @Override
    public List<Long> putItems(List<? extends Store> stores) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<? extends Store> stores) {
        return null;
    }

    @Override
    public int delete(Store store) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(Store store) {
        return null;
    }

    @Override
    public List<Long> delete(List<? extends Store> stores) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<? extends Store> stores) {
        return null;
    }

    @Override
    public Store getItem(String id) {
        return null;
    }

    @Override
    public Maybe<Store> getItemRx(String id) {
        return null;
    }

    @Override
    public List<Store> getItems() {
        return null;
    }

    @Override
    public Maybe<List<Store>> getItemsRx() {
        return null;
    }

    @Override
    public List<Store> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Store>> getItemsRx(int limit) {
        return null;
    }


}
*/
