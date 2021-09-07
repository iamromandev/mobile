package com.dreampany.lca.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.beardedhen.androidbootstrap.BootstrapDropDown;
import com.dreampany.framework.data.enums.UiState;
import com.dreampany.framework.data.model.Response;
import com.dreampany.framework.injector.annote.FragmentScope;
import com.dreampany.framework.misc.exceptions.EmptyException;
import com.dreampany.framework.misc.exceptions.ExtraException;
import com.dreampany.framework.ui.fragment.BaseFragment;
import com.dreampany.framework.ui.listener.OnVerticalScrollListener;
import com.dreampany.framework.util.ViewUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.databinding.FragmentMarketBinding;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.adapter.MarketAdapter;
import com.dreampany.lca.ui.model.ExchangeItem;
import com.dreampany.lca.ui.model.MarketItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.lca.vm.ExchangeViewModel;
import com.dreampany.lca.vm.MarketViewModel;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;

//import com.dreampany.frame.data.enums.EventType;


/**
 * Created by Hawladar Roman on 5/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@FragmentScope
public class MarketFragment
        extends BaseFragment
        implements BootstrapDropDown.OnDropDownItemClickListener {

    @Inject
    ViewModelProvider.Factory factory;
    private FragmentMarketBinding binding;
    private ExchangeViewModel evm;
    private MarketViewModel mvm;
    private MarketAdapter adapter;
    private OnVerticalScrollListener scroller;
    private SwipeRefreshLayout refresh;
    private ExpandableLayout expandable;
    private BootstrapDropDown currencyDropDown;
    private RecyclerView recycler;
    private String currency;

    @Inject
    public MarketFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_market;
    }

    @NotNull
    @Override
    public String getScreen() {
        return Constants.Companion.coinMarket(getAppContext());
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {
        initView();
        initRecycler();
        evm.start();
        evm.loads(false);
    }

    @Override
    protected void onStopUi() {
        evm.clear();
        mvm.clear();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        processUiState(UiState.HIDE_PROGRESS);
        super.onPause();
       // evm.removeMultipleSubscription();
    }

    @Override
    public void onRefresh() {
        mvm.loads(currency, true);
    }

    @Override
    public void onClick(@NonNull View v) {
/*        switch (v.getId()) {
            case R.id.button_empty:
                mvm.loads(currency, true);
                break;
        }*/
    }

    @Override
    public void onItemClick(ViewGroup parent, View v, int id) {
        String currency = binding.dropDownCurrency.getDropdownData()[id];
        if (!this.currency.equalsIgnoreCase(currency)) {
            this.currency = currency;
            binding.dropDownCurrency.setText(currency);
            mvm.removeSingleSubscription();
            mvm.loads(currency, true);
        }
    }

    @Override
    public boolean onItemClick(View view, int position) {
        if (position != RecyclerView.NO_POSITION) {
            MarketItem item = adapter.getItem(position);
            mvm.openMarket(this, Objects.requireNonNull(item).getItem().getMarket());
            return true;
        }
        return false;
    }

    private void initView() {
        binding = (FragmentMarketBinding) super.binding;
        refresh = binding.layoutRefresh;
        expandable = binding.layoutTop.layoutExpandable;
        recycler = binding.layoutRecycler.recycler;

        currencyDropDown = binding.dropDownCurrency;

        ViewUtil.Companion.setSwipe(refresh, this);
        //ViewUtil.setClickListener(this, R.id.button_empty);
        currencyDropDown.setOnDropDownItemClickListener(this);

        evm = ViewModelProviders.of(this, factory).get(ExchangeViewModel.class);
        mvm = ViewModelProviders.of(this, factory).get(MarketViewModel.class);
        UiTask<Coin> uiTask = getCurrentTask(true);
        evm.setTask(uiTask);
        mvm.setTask(uiTask);

        mvm.observeUiState(this, this::processUiState);
        //evm.observeEvent(this, this::processEvent);
        //mvm.observeEvent(this, this::processEvent);
        evm.observeOutputs(this, this::processExchangeResponse);
        mvm.observeOutputs(this, this::processMarketResponse);
        currency = getString(R.string.usd);
    }

    private void initRecycler() {
        binding.setItems(new ObservableArrayList<>());
        adapter = new MarketAdapter(this);
        adapter.setStickyHeaders(false);
        scroller = new OnVerticalScrollListener();
        ViewUtil.Companion.setRecycler(
                adapter,
                recycler,
                new SmoothScrollLinearLayoutManager(Objects.requireNonNull(getContext())),
                new FlexibleItemDecoration(getContext())
                        .addItemViewType(R.layout.item_market, mvm.getItemOffset())
                        .withEdge(true),
                null,
                scroller,
                null
        );
    }

    private void processUiState(UiState state) {
        switch (state) {
            case SHOW_PROGRESS:
                binding.layoutRefresh.setRefreshing(true);
                break;
            case HIDE_PROGRESS:
                binding.layoutRefresh.setRefreshing(false);
                break;
            case ERROR:
                break;
            case OFFLINE:
                expandable.expand();
                break;
            case ONLINE:
                expandable.collapse();
                break;
            case EXTRA:
                processUiState(adapter.isEmpty() ? UiState.EMPTY : UiState.CONTENT);
                break;
            case CONTENT:
                binding.stateful.showContent();
                break;
        }
    }

/*    private void processEvent(EventType eventType) {
        switch (eventType) {
            case NEW:
                break;
        }
    }*/


    private void processExchangeResponse(Response<List<ExchangeItem>> response) {
        if (response instanceof Response.Progress) {
            Response.Progress progress = (Response.Progress) response;
            processExchangeProgress(progress.getLoading());
        } else if (response instanceof Response.Failure) {
            Response.Failure failure = (Response.Failure) response;
            processFailure(failure.getError());
        } else if (response instanceof Response.Result) {
            Response.Result<List<ExchangeItem>> result = (Response.Result<List<ExchangeItem>>) response;
            processExchangeSuccess(result.getData());
        }
    }


    private void processMarketResponse(Response<List<MarketItem>> response) {
        if (response instanceof Response.Progress) {
            Response.Progress progress = (Response.Progress) response;
            processMarketProgress(progress.getLoading());
        } else if (response instanceof Response.Failure) {
            Response.Failure failure = (Response.Failure) response;
            processFailure(failure.getError());
        } else if (response instanceof Response.Result) {
            Response.Result<List<MarketItem>> result = (Response.Result<List<MarketItem>>) response;
            processMarketSuccess(result.getData());
        }
    }


    private void processExchangeProgress(boolean loading) {
        if (!adapter.isEmpty()) {
            return;
        }
        if (loading) {
            mvm.updateUiState(UiState.SHOW_PROGRESS);
        } else {
            mvm.updateUiState(UiState.HIDE_PROGRESS);
        }
    }



    private void processMarketProgress(boolean loading) {
        if (loading) {
            mvm.updateUiState(UiState.SHOW_PROGRESS);
        } else {
            mvm.updateUiState(UiState.HIDE_PROGRESS);
        }
    }


    private void processFailure(Throwable error) {
        if (error instanceof IOException || error.getCause() instanceof IOException) {
            mvm.updateUiState(UiState.OFFLINE);
        } else if (error instanceof EmptyException) {
            mvm.updateUiState(UiState.EMPTY);
        } else if (error instanceof ExtraException) {
            mvm.updateUiState(UiState.EXTRA);
        }
    }


    private void processExchangeSuccess(List<ExchangeItem> items) {
        if (items == null || items.isEmpty()) {
            //todo show message
        } else {
            List<String> list = new ArrayList<>(items.size());
            for (ExchangeItem item : items) {
                list.add(item.getItem().getToSymbol());
            }
            String[] currencies = list.toArray(new String[0]);
            String currency = binding.dropDownCurrency.getText().toString();
            if (!list.contains(currency)) {
                currency = currencies[0];
                binding.dropDownCurrency.setText(currency);
            }
            binding.dropDownCurrency.setDropdownData(currencies);
//            mvm.removeSingleSubscription();
            mvm.loads(currency, true);
        }
    }


    private void processMarketSuccess(List<MarketItem> items) {
        adapter.clear();
        adapter.addItems(items);
    }
}
