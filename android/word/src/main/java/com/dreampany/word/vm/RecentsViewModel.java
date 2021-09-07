package com.dreampany.word.vm;

import android.app.Application;
import com.annimon.stream.Stream;
import com.dreampany.framework.misc.AppExecutors;
import com.dreampany.framework.misc.ResponseMapper;
import com.dreampany.framework.misc.RxMapper;
import com.dreampany.framework.misc.SmartMap;
import com.dreampany.framework.ui.adapter.SmartAdapter;
import com.dreampany.framework.util.DataUtil;
import com.dreampany.framework.ui.vm.BaseViewModel;
import com.dreampany.network.manager.NetworkManager;
import com.dreampany.word.data.misc.StateMapper;
import com.dreampany.word.data.model.Word;
import com.dreampany.word.data.source.repository.WordRepository;
import com.dreampany.word.misc.Constants;
import com.dreampany.word.ui.model.UiTask;
import com.dreampany.word.ui.model.WordItem;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hawladar Roman on 2/9/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class RecentsViewModel extends BaseViewModel<Word, WordItem, UiTask<Word>> {

    private static final long initialDelay = Constants.Time.INSTANCE.getWordPeriod();
    private static final long period = Constants.Time.INSTANCE.getWordPeriod();
    private static final int retry = 3;

    private final NetworkManager network;
    private final WordRepository repo;
    private final StateMapper stateMapper;
    private SmartAdapter.Callback<WordItem> uiCallback;
    private Disposable updateDisposable;

    @Inject
    RecentsViewModel(Application application,
                     RxMapper rx,
                     AppExecutors ex,
                     ResponseMapper rm,
                     NetworkManager network,
                     WordRepository repo,
                     StateMapper stateMapper) {
        super(application, rx, ex, rm);
        this.network = network;
        this.repo = repo;
        this.stateMapper = stateMapper;
        //network.observe(this::onResult, true);
    }

    @Override
    public void clear() {
        this.uiCallback = null;
        removeSubscription(updateDisposable);
        //network.deObserve(this::onResult, true);
        super.clear();
    }

    public void setUiCallback(SmartAdapter.Callback<WordItem> callback) {
        this.uiCallback = callback;
    }


    public void loads(boolean fresh) {
        if (!takeAction(fresh, getMultipleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getRecentItemsRx())
                .doOnSubscribe(subscription -> {
                    postProgress(true);
                })
                .subscribe(                        result -> {
                    postProgress(false);
                    //postResult(result);
                }, error -> {
                    //postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
        //updateVisibleItems();
    }

    public void update() {
        if (hasDisposable(updateDisposable)) {
            Timber.v("Updater Running...");
            return;
        }
/*        updateDisposable = getRx()
                .backToMain(updateItemInterval())
                .subscribe(this::postResult, this::postFailure);
        addSubscription(updateDisposable);*/
    }

    public void load(String word) {
        if (!takeAction(true, getSingleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemRx(word))
                .doOnSubscribe(subscription -> postProgress(true))
                .subscribe(
                        result -> {
                            postProgress(false);
                            //postResult(result);
                        },
                        error ->
                        {//postFailureMultiple(new MultiException(error, new ExtraException()));
                        }
                );
        addSingleSubscription(disposable);
    }

    public void removeUpdateDisposable() {
        removeSubscription(updateDisposable);
    }

    private Maybe<List<WordItem>> getRecentItemsRx() {
        return Maybe.empty();
        /*        return repo.getRecentItemsRx()
                .onErrorResumeNext(Maybe.empty())
                .flatMap((Function<List<Word>, MaybeSource<List<WordItem>>>) this::getItemsRx);*/
    }

    private Maybe<WordItem> getItemRx(String word) {
        return repo.getItemRx(word, true).map(this::getItem);
    }

    private Maybe<List<WordItem>> getItemsRx(List<Word> words) {
        return Maybe.fromCallable(() -> {
            //remove already in UI
            List<Word> filtered = new ArrayList<>();
            SmartMap<String, WordItem> ui = getUiMap();
            Stream.of(words).forEach(word -> {
                if (!ui.contains(word.getId())) {
                    filtered.add(word);
                }
            });
            //Collections.sort(filtered, (left, right) -> left.getWord().compareTo(right.getWord()));
            List<WordItem> items = new ArrayList<>(filtered.size());
            for (Word word : filtered) {
                WordItem item = getItem(word);
                items.add(item);
            }
            return items;
        });
    }

    private Flowable<WordItem> updateItemInterval() {
        Flowable<WordItem> flowable = Flowable
                .interval(initialDelay, period, TimeUnit.MILLISECONDS, getRx().io())
                .map(tick -> {
                    WordItem next = null;
                    if (uiCallback != null) {
                        List<WordItem> items = uiCallback.getVisibleItems();
                        if (!DataUtil.isEmpty(items)) {
                            for (WordItem item : items) {
        /*                        if (!item.hasState(ItemState.STATE, ItemSubstate.FULL)) {
                                    Timber.d("Next Item to updateVisibleItemIf %s", item.getItemRx().getWord());
                                    next = updateItemRx(item.getItemRx()).blockingGet();
                                    break;
                                }*/
                            }
                        }
                    }
                    return next;
                });
        return flowable;
    }

    private Maybe<WordItem> updateItemRx(Word item) {
        return repo.getItemRx(item.getId(), true).map(this::getItem);
    }

    private WordItem getItem(Word word) {
        SmartMap<String, WordItem> map = getUiMap();
        WordItem item = map.get(word.getId());
        if (item == null) {
            item = WordItem.Companion.getItem(word);
            map.put(word.getId(), item);
        }
        item.setItem(word);
        adjustState(item);
        adjustFlag(item);
        return item;
    }

    private void adjustState(WordItem item) {
        //List<State> states = repo.getStates(item.getItemRx());
        //Stream.of(states).forEach(state -> item.addState(stateMapper.toState(state.getState()), stateMapper.toSubstate(state.getSubstate())));
    }

    private void adjustFlag(WordItem item) {
/*        boolean flagged = repo.isFavorite(coin);
        item.setFavorite(flagged);*/
    }
}
