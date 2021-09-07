/*
package com.dreampany.word.data.source.vision;

import android.graphics.Bitmap;

import com.dreampany.frame.util.TextUtil;
import com.dreampany.vision.VisionApi;
import com.dreampany.word.data.misc.WordMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.api.WordDataSource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Maybe;

*/
/**
 * Created by Hawladar Roman on 9/27/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 *//*

@Singleton
public class VisionWordDataSource implements WordDataSource {

    private final WordMapper mapper;
    private final VisionApi vision;

    public VisionWordDataSource(WordMapper mapper,
                                VisionApi vision) {
        this.mapper = mapper;
        this.vision = vision;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return null;
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
    public List<Long> putItems(List<? extends Word> words) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<? extends Word> words) {
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
    public List<Long> delete(List<? extends Word> words) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<? extends Word> words) {
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
    public List<Word> getItems() {
        return null;
    }

    @Override
    public Maybe<List<Word>> getItemsRx() {
        return null;
    }

    @Override
    public List<Word> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Word>> getItemsRx(int limit) {
        return null;
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
    public Word getItem(String word, boolean full) {
        return null;
    }

    @Override
    public Maybe<Word> getItemRx(String word, boolean full) {
        return null;
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
    public ArrayList<Word> getSearchItems(String query, int limit) {
        return null;
    }

    @Override
    public ArrayList<Word> getCommonItems() {
        return null;
    }

    @Override
    public ArrayList<Word> getAlphaItems() {
        return null;
    }

    @Override
    public Maybe<List<Word>> getItemsRx(Bitmap bitmap) {
        return Maybe.fromCallable(() -> {
            String text = vision.getText(bitmap);
            List<String> items = TextUtil.getWords(text);
            List<Word> result = new ArrayList<>();
            for (String item : items) {
                result.add(mapper.toItem(item.toLowerCase()));
            }
            return result;
        });
    }

    @Override
    public List<String> getRawWords() {
        return null;
    }

    @Override
    public Maybe<List<String>> getRawWordsRx() {
        return null;
    }
}
*/
