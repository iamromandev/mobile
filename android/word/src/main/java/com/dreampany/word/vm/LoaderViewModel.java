package com.dreampany.word.vm;

import android.app.Application;

import com.dreampany.framework.data.model.Response;
import com.dreampany.framework.data.model.State;
import com.dreampany.framework.data.source.repository.StateRepository;
import com.dreampany.framework.misc.AppExecutors;
import com.dreampany.framework.misc.ResponseMapper;
import com.dreampany.framework.misc.Runner;
import com.dreampany.framework.misc.RxMapper;
import com.dreampany.framework.misc.exceptions.ExtraException;
import com.dreampany.framework.misc.exceptions.MultiException;
import com.dreampany.framework.util.AndroidUtil;
import com.dreampany.framework.util.DataUtil;
import com.dreampany.framework.util.TextUtil;
import com.dreampany.framework.util.TimeUtil;
import com.dreampany.framework.ui.vm.BaseViewModel;
import com.dreampany.word.R;
import com.dreampany.word.data.enums.ItemState;
import com.dreampany.word.data.enums.ItemSubtype;
import com.dreampany.word.data.enums.ItemType;
import com.dreampany.word.data.model.Load;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.pref.LoadPref;
import com.dreampany.word.data.source.repository.WordRepository;
import com.dreampany.word.misc.Constants;
import com.dreampany.word.ui.model.LoadItem;
import com.dreampany.word.ui.model.UiTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class LoaderViewModel extends BaseViewModel<Load, LoadItem, UiTask<Load>> {

    private final Object guard = new Object();

    private final LoadPref pref;
    private final StateRepository stateRepo;
    private final WordRepository wordRepo;
    private final List<Word> commonWords;
    private final List<Word> alphaWords;
    private final int totalResId;
    private volatile LoadThread loadThread;

    @Inject
    LoaderViewModel(Application application,
                    RxMapper rx,
                    AppExecutors ex,
                    ResponseMapper rm,
                    LoadPref pref,
                    StateRepository stateRepo,
                    WordRepository wordRepo) {
        super(application, rx, ex, rm);
        this.pref = pref;
        this.stateRepo = stateRepo;
        this.wordRepo = wordRepo;

        commonWords = Collections.synchronizedList(new ArrayList<>());
        alphaWords = Collections.synchronizedList(new ArrayList<>());

        totalResId = R.string.total_words;
    }

    @NotNull
    @Override
    protected Maybe<String> getSubtitle() {
        return Maybe.fromCallable(() -> {
            int total = getStateCount(ItemType.WORD, ItemSubtype.DEFAULT, ItemState.RAW);
            String title = TextUtil.getString(getApplication(), totalResId, total);
            return title;
        });
    }

    public void pause() {
        takeAction(true, getMultipleDisposable());
    }

    public void loads() {
        boolean commonLoaded = pref.isCommonLoaded();
        if (!commonLoaded) {
            loadCommons();
            return;
        }
        if (!pref.isAlphaLoaded()) {
            loadAlphas();
        }
    }

    public void loadCommons() {
        if (!takeAction(false, getMultipleDisposable())) {
            return;
        }
        Timber.v("loadCommons running...");
        Disposable disposable = getRx()
                .backToMain(getCommonItemsRx())
                .subscribe(result -> {
                    postResult(Response.Type.GET, result);
                    loadAlphas();
                }, error -> {
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }


    public void loadAlphas() {
        if (!takeAction(false, getMultipleDisposable())) {
            Timber.v("loadAlphas already running...");
            return;
        }
        Timber.v("loadAlphas running...");
        Disposable disposable = getRx()
                .backToMain(getAlphaItemsRx())
                .subscribe(result -> {
                    //loadAlphas();
                    postResult(Response.Type.GET, result);
                }, error -> {
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }


    private Maybe<LoadItem> getCommonItemsRx() {
        return Maybe.fromCallable(() -> {
            LoadItem item = LoadItem.getSimpleItem();
            if (pref.isCommonLoaded()) {
                int current = getStateCount(ItemType.WORD, ItemSubtype.DEFAULT, ItemState.RAW);
                Load load = new Load(current, current);
                item.setItem(load);
                return item;
            }

            if (commonWords.size() != Constants.Count.WORD_COMMON) {
                List<Word> words = wordRepo.getCommonItems();
                commonWords.clear();
                commonWords.addAll(words);
            }
            Word last = pref.getLastWord();
            int lastIndex = last != null ? commonWords.indexOf(last) : -1;
            int current = getStateCount(ItemType.WORD, ItemSubtype.DEFAULT, ItemState.RAW);
            Load load = new Load(current, current);
            item.setItem(load);
            getEx().postToUi(() -> postResult(Response.Type.GET, item));
            if (lastIndex > 0) {
                DataUtil.removeFirst(commonWords, lastIndex + 1);
            }

            while (!commonWords.isEmpty()) {
                List<Word> words = DataUtil.takeFirst(commonWords, Constants.Count.WORD_PAGE);
                List<Long> result = putItems(words, ItemSubtype.DEFAULT, ItemState.RAW);

                if (DataUtil.isEqual(words, result)) {
                    Word lastWord = DataUtil.pullLast(words);
                    pref.setLastWord(lastWord);
                    current = getStateCount(ItemType.WORD, ItemSubtype.DEFAULT, ItemState.RAW);
                    load.setCurrent(current);
                    load.setTotal(current);

                    Timber.v("%d Last Common Word = %s", current, lastWord.toString());
                    getEx().postToUi(() -> postResult(Response.Type.GET, item));
                    AndroidUtil.Companion.sleep(100);
                }
            }
            if (commonWords.isEmpty()) {
                pref.commitCommonLoaded();
                pref.clearLastWord();
            }
            return item;
        });
    }

    private Maybe<LoadItem> getAlphaItemsRx() {
        return Maybe.fromCallable(() -> {
            LoadItem item = LoadItem.getSimpleItem();
            if (pref.isAlphaLoaded()) {
                int current = getStateCount(ItemType.WORD, ItemSubtype.DEFAULT, ItemState.RAW);
                Load load = new Load(current, current);
                item.setItem(load);
                return item;
            }

            if (alphaWords.size() != Constants.Count.WORD_ALPHA) {
                List<Word> words = wordRepo.getAlphaItems();
                alphaWords.clear();
                alphaWords.addAll(words);
            }
            Word last = pref.getLastWord();
            int lastIndex = last != null ? alphaWords.indexOf(last) : 0;
            int current = getStateCount(ItemType.WORD, ItemSubtype.DEFAULT, ItemState.RAW);
            Load load = new Load(current, current);
            item.setItem(load);
            getEx().postToUi(() -> postResult(Response.Type.GET, item));

            if (lastIndex > 0) {
                DataUtil.removeFirst(alphaWords, lastIndex + 1);
            }
            while (!alphaWords.isEmpty()) {
                List<Word> words = DataUtil.takeFirst(alphaWords, Constants.Count.WORD_PAGE);
                List<Long> result = putItems(words, ItemSubtype.DEFAULT, ItemState.RAW);

                if (DataUtil.isEqual(words, result)) {
                    Word lastWord = DataUtil.pullLast(words);
                    pref.setLastWord(lastWord);
                    current = getStateCount(ItemType.WORD, ItemSubtype.DEFAULT, ItemState.RAW);
                    load.setCurrent(current);
                    load.setTotal(current);
                    Timber.v("Current Alpha Word = %d %s", current, lastWord.toString());
                    getEx().postToUi(() -> postResult(Response.Type.GET, item));
                    AndroidUtil.Companion.sleep(100);
                }
            }
            if (alphaWords.isEmpty()) {
                pref.commitAlphaLoaded();
                pref.clearLastWord();
            }
            return item;
        });
    }

    private boolean hasRecent(Word word) {
        if (word.getId().length() < Constants.Count.WORD_RECENT_LETTER) {
            return false;
        }
        if (hasState(word, ItemSubtype.RECENT)) {
            return false;
        }
        return true;
    }


    public int getStateCount(ItemType type, ItemSubtype subtype, ItemState state) {
        return stateRepo.getCount(type.name(), subtype.name(), state.name());
    }

    public List<Long> putItems(List<Word> words, ItemSubtype subtype, ItemState state) {
        List<Long> result = wordRepo.putItems(words);
        if (DataUtil.isEqual(words, result)) {
            result = putStates(words, subtype, state);
        }
        return result;
    }

    public boolean hasState(Word word, ItemSubtype subtype) {
        boolean stated = stateRepo.getCountById(word.getId(), ItemType.WORD.name(), subtype.name()) > 0;
        return stated;
    }

    public List<Long> putStates(List<Word> words, ItemSubtype subtype, ItemState state) {
        List<State> states = new ArrayList<>();
        for (Word word : words) {
            State s = new State(word.getId(), ItemType.WORD.name(), subtype.name(), state.name());
            s.setTime(TimeUtil.currentTime());
            states.add(s);
        }
        List<Long> result = stateRepo.putItems(states);
        return result;
    }


    private void startLoadThread() {
        synchronized (guard) {
            if (loadThread == null || !loadThread.isRunning()) {
                loadThread = new LoadThread();
                loadThread.start();
            }
            loadThread.notifyRunner();
        }
    }

    private void stopRequestThread() {
        synchronized (guard) {
            if (loadThread != null) {
                loadThread.stop();
            }
        }
    }


    private class LoadThread extends Runner {

        LoadThread() {

        }

        @Override
        protected boolean looping() {
            //1. download listing if expired or not performed
            //2. Take one by one coins from server


            return true;
        }
    }
}
