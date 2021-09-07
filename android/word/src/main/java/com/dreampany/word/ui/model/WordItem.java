/*
package com.dreampany.word.ui.model;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.dreampany.frame.ui.model.BaseItem;
import com.dreampany.word.R;
import com.dreampany.word.data.enums.ItemState;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.ui.adapter.WordAdapter;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

*/
/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 *//*

public class WordItem extends BaseItem<Word, WordItem.ViewHolder> {

    private Set<ItemState> states;
    private boolean recent;
    private Map<String, String> translations;
    private String translation;
    private boolean favorite;
    private long time;

    private WordItem(Word word, @LayoutRes int layoutId) {
        super(word, layoutId);
        states = new HashSet<>();
        translations = new HashMap<>();
    }

    public static WordItem getSimpleItem(Word item) {
        return new WordItem(item, R.layout.item_word);
    }

    public void addState(ItemState state) {
        states.add(state);
    }

    public boolean hasState(ItemState state) {
        return states.contains(state);
    }

    public void setRecent(boolean recent) {
        this.recent = recent;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isRecent() {
        return recent;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public long getTime() {
        return time;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    @Override
    public boolean equals(Object inObject) {
        if (this == inObject) return true;
        if (inObject == null || getClass() != inObject.getClass()) return false;
        WordItem item = (WordItem) inObject;
        return Objects.equal(item.getItem(), getItem());
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new SimpleViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position, List<Object> payloads) {
        holder.bind(position, this);
    }

    @Override
    public boolean filter(Serializable constraint) {
        return item.getId().toLowerCase().startsWith(((String) constraint).toLowerCase());
    }

    public void addTranslation(String language, String translatedWord) {
        translations.put(language, translatedWord);
    }

    public boolean hasTranslation(String language) {
        if (language == null) {
            return false;
        }
        return translations.containsKey(language);
    }

    public String getTranslationBy(String language) {
        if (hasTranslation(language)) {
            return translations.get(language);
        }
        return null;
    }

    public String getTranslation() {
*/
/*        if (hasTranslation(language.get())) {
            return translations.get(language.get());
        }*//*

        return translation;
    }

    static abstract class ViewHolder extends BaseItem.ViewHolder {

        final WordAdapter adapter;

        ViewHolder(@NonNull View view, @NonNull FlexibleAdapter adapter) {
            super(view, adapter);
            this.adapter = (WordAdapter) adapter;
        }

        abstract void bind(int position, WordItem item);
    }

    static final class SimpleViewHolder extends ViewHolder {

        TextView word;
        TextView partOfSpeech;

        SimpleViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
            super(view, adapter);
            word = view.findViewById(R.id.text_word);
            partOfSpeech = view.findViewById(R.id.text_part_of_speech);
            pronunciation = view.findViewById(R.id.text_pronunciation);
            word.setOnClickListener(super.adapter.getClickListener());
        }
        TextView pronunciation;

        @Override
        void bind(int position, WordItem item) {
            Word word = item.getItem();
            this.word.setText(word.getId());
            this.partOfSpeech.setText(word.getPartOfSpeech());
            this.pronunciation.setText(word.getPronunciation());
        }
    }
}
*/
