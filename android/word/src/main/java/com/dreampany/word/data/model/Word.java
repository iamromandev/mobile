/*
package com.dreampany.word.data.model;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

import com.dreampany.frame.data.model.Base;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.word.misc.Constants;
import com.google.common.base.Objects;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by Hawladar Roman on 1/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 *//*


@Entity(indices = {@Index(value = {Constants.Word.ID}, unique = true)},
        primaryKeys = {Constants.Word.ID})
@IgnoreExtraProperties
public class Word extends Base {

    @ColumnInfo(name = Constants.Word.PART_OF_SPEECH)
    @PropertyName(Constants.Word.PART_OF_SPEECH)
    private String partOfSpeech;
    private String pronunciation;
    private List<Definition> definitions;
    private List<String> examples;
    @Ignore
    private List<String> synonyms;
    @Ignore
    private List<String> antonyms;
    private List<String> categories;
    private List<String> tags;
    private List<String> notes;
    private int popularity;

    @Ignore
    public Word() {
    }

    public Word(@NotNull String id) {
        this.id = id;
    }

    @Ignore
    private Word(Parcel in) {
        super(in);
        partOfSpeech = in.readString();
        pronunciation = in.readString();
        if (in.readByte() == 0x01) {
            definitions = new ArrayList<>();
            in.readList(definitions, Definition.class.getClassLoader());
        } else {
            definitions = null;
        }
        examples = in.createStringArrayList();
        synonyms = in.createStringArrayList();
        antonyms = in.createStringArrayList();
        categories = in.createStringArrayList();
        tags = in.createStringArrayList();
        notes = in.createStringArrayList();
        popularity = in.readInt();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(partOfSpeech);
        dest.writeString(pronunciation);
        if (definitions == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(definitions);
        }
        dest.writeStringList(examples);
        dest.writeStringList(synonyms);
        dest.writeStringList(antonyms);
        dest.writeStringList(categories);
        dest.writeStringList(tags);
        dest.writeStringList(notes);
        dest.writeInt(popularity);
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    @Override
    public boolean equals(Object in) {
        if (this == in) return true;
        if (in == null || getClass() != in.getClass()) return false;

        Word item = (Word) in;
        return Objects.equal(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


    @PropertyName(Constants.Word.PART_OF_SPEECH)
    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }

    public void setExamples(List<String> examples) {
        this.examples = examples;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public void setAntonyms(List<String> antonyms) {
        this.antonyms = antonyms;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    @PropertyName(Constants.Word.PART_OF_SPEECH)
    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public List<String> getExamples() {
        return examples;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public List<String> getAntonyms() {
        return antonyms;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getNotes() {
        return notes;
    }

    public int getPopularity() {
        return popularity;
    }

    //special public api
    public void addDefinition(Definition definition) {
        if (definitions == null) {
            definitions = new ArrayList<>();
        }
        if (!definitions.contains(definition)) {
            definitions.add(definition);
        }
    }

    public void addExample(String example) {
        if (examples == null) {
            examples = new ArrayList<>();
        }
        if (!examples.contains(example)) {
            examples.add(example);
        }
    }

    public void addSynonym(String synonym) {
        if (synonyms == null) {
            synonyms = new ArrayList<>();
        }
        if (!synonyms.contains(synonym)) {
            synonyms.add(synonym);
        }
    }

    public void addAntonym(String antonym) {
        if (antonyms == null) {
            antonyms = new ArrayList<>();
        }
        if (!antonyms.contains(antonym)) {
            antonyms.add(antonym);
        }
    }

    public void addCategory(String category) {
        if (categories == null) {
            categories = new ArrayList<>();
        }
        if (!categories.contains(category)) {
            categories.add(category);
        }
    }

    public void addTag(String tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }

    public void addNote(String note) {
        if (notes == null) {
            notes = new ArrayList<>();
        }
        if (!notes.contains(note)) {
            notes.add(note);
        }
    }

    public boolean hasDefinitions() {
        return !(definitions == null || definitions.isEmpty());
    }

    public boolean hasExamples() {
        return !(examples == null || examples.isEmpty());
    }

    public boolean hasSynonyms() {
        return !(synonyms == null || synonyms.isEmpty());
    }

    public boolean hasAntonyms() {
        return !(antonyms == null || antonyms.isEmpty());
    }

    public boolean hasCategories() {
        return !(categories == null || categories.isEmpty());
    }

    public boolean hasTags() {
        return !(tags == null || tags.isEmpty());
    }

    public boolean hasNotes() {
        return !(notes == null || notes.isEmpty());
    }

    public boolean containsDefinition(Definition definition) {
        if (hasDefinitions()) {
            return definitions.contains(definition);
        }
        return false;
    }

    public boolean containsExample(String example) {
        if (hasExamples()) {
            return examples.contains(example);
        }
        return false;
    }

    public boolean containsSynonym(String synonym) {
        if (hasSynonyms()) {
            return synonyms.contains(synonym);
        }
        return false;
    }

    public boolean containsAntonym(String antonym) {
        if (hasAntonyms()) {
            return antonyms.contains(antonym);
        }
        return false;
    }

    public boolean containsCategory(String category) {
        if (hasCategories()) {
            return categories.contains(category);
        }
        return false;
    }

    public boolean containsTag(String tag) {
        if (hasTags()) {
            return tags.contains(tag);
        }
        return false;
    }

    public boolean containsNote(String note) {
        if (hasNotes()) {
            return notes.contains(note);
        }
        return false;
    }

    @Exclude
    public boolean isToday() {
        return getTime() > 0;
    }

    @Override
    public String toString() {
        return "Word (" + id + ") == " + id;
    }

    public void copyWord(Word from) {
        id = from.getId();
        partOfSpeech = from.getPartOfSpeech();
        pronunciation = from.getPronunciation();
        definitions = from.getDefinitions();
        examples = from.getExamples();
        synonyms = from.getSynonyms();
        antonyms = from.getAntonyms();
        categories = from.getCategories();
        tags = from.getTags();
        notes = from.getNotes();
        popularity = from.getPopularity();
    }

    public boolean hasPartial() {
        if (DataUtil.isEmpty(new String[]{partOfSpeech, pronunciation}) && DataUtil.isEmpty(definitions) && DataUtil.isEmpty(examples)) {
            return false;
        }
        return true;
    }

    public boolean hasFull() {
        if (!hasPartial()) {
            return false;
        }
        if (DataUtil.isEmpty(synonyms) && DataUtil.isEmpty(antonyms)) {
            return false;
        }
        return true;
    }
}
*/
