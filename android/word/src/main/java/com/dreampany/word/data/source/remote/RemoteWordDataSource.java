/*
package com.dreampany.word.data.source.remote;

import android.graphics.Bitmap;

import com.annimon.stream.Stream;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.network.manager.NetworkManager;
import com.dreampany.word.api.wordnik.WordnikManager;
import com.dreampany.word.api.wordnik.WordnikWord;
import com.dreampany.word.data.misc.WordMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.api.WordDataSource;
import com.dreampany.word.misc.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Maybe;
import timber.log.Timber;

*/
/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 *//*

@Singleton
public class RemoteWordDataSource implements WordDataSource {

    private final NetworkManager network;
    private final WordMapper mapper;
    private final WordnikManager wordnik;

    public RemoteWordDataSource(NetworkManager network,
                                WordMapper mapper,
                                WordnikManager wordnik) {
        this.network = network;
        this.mapper = mapper;
        this.wordnik = wordnik;
    }

    @Override
    public Word getTodayItem() {
        return null;
    }

    @Override
    public Maybe<Word> getTodayItemRx() {
        return null;
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
    public boolean isExists(Word word) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Word word) {
        return null;
    }

    @Override
    public long putItem(Word word) {
        return 0;
    }

    @Override
    public Maybe<Long> putItemRx(Word word) {
        return null;
    }

    @Override
    public int delete(Word word) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(Word word) {
        return null;
    }

    @Override
    public Word getItem(String id) {
        return null;
    }

    @Override
    public Maybe<Word> getItemRx(String id) {
        return null;
    }

    @Override
    public Word getItem(String word, boolean full) {
        WordnikWord item = wordnik.getWord(word, Constants.Limit.WORD_RESOLVE);
        Timber.v("Wordnik Result %s", item.getWord());
        return mapper.toItem(word, item, true);
    }

    @Override
    public Maybe<Word> getItemRx(String word, boolean full) {
        return Maybe.fromCallable(() -> getItem(word, full));
    }

    @Override
    public List<Word> getItems(List<String> ids) {
        return null;
    }

    @Override
    public Maybe<List<Word>> getItemsRx(List<String> ids) {
        return null;
    }

    @Override
    public List<Word> getSearchItems(String query, int limit) {
        List<WordnikWord> items = wordnik.search(query, limit);
        List<Word> result = new ArrayList<>();
        if (!DataUtil.isEmpty(items)) {
            Stream.of(items).forEach(word -> result.add(mapper.toItem(word, false)));
        }
        return result;
    }

*/
/*    @Override
    public Maybe<List<Word>> getSearchItemsRx(String query) {
        return null;
    }

    @Override
    public Maybe<List<Word>> getSearchItemsRx(String query, int limit) {
        return Maybe.fromCallable(() -> {
            List<WordnikWord> items = wordnik.search(query, limit);
            List<Word> result = new ArrayList<>();
            if (!DataUtil.isEmpty(items)) {
                Stream.of(items).forEach(word -> result.add(mapper.toItem(word, false)));
            }
            return result;
        });
    }*//*


    @Override
    public List<Word> getCommonItems() {
        return null;
    }

    @Override
    public List<Word> getAlphaItems() {
        return null;
    }

    @Override
    public Maybe<List<Word>> getItemsRx(Bitmap bitmap) {
        return null;
    }

    @Override
    public List<String> getRawWords() {
        return null;
    }

    @Override
    public Maybe<List<String>> getRawWordsRx() {
        return null;
    }


    @NotNull
    @Override
    public List<Long> delete(@NotNull List<? extends Word> words) {
        return null;
    }

    @NotNull
    @Override
    public Maybe<List<Long>> deleteRx(@NotNull List<? extends Word> words) {
        return null;
    }

    @NotNull
    @Override
    public List<Long> putItems(@NotNull List<? extends Word> words) {
        return null;
    }

    @NotNull
    @Override
    public Maybe<List<Long>> putItemsRx(@NotNull List<? extends Word> words) {
        return null;
    }

    @NotNull
    @Override
    public List<Word> getItems() {
        return null;
    }

    @NotNull
    @Override
    public Maybe<List<Word>> getItemsRx() {
        return null;
    }

    @NotNull
    @Override
    public List<Word> getItems(int limit) {
        return null;
    }

    @NotNull
    @Override
    public Maybe<List<Word>> getItemsRx(int limit) {
        return null;
    }
}
*/
