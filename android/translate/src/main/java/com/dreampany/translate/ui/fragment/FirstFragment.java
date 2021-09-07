package com.dreampany.translate.ui.fragment;

import android.os.Bundle;

import com.dreampany.framework.misc.FragmentScope;
import com.dreampany.framework.ui.fragment.BaseFragment;
import com.dreampany.translate.R;

import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 6/20/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@FragmentScope
public class FirstFragment extends BaseFragment {

    // private UiState state = UiState.CONTENT;

    @Inject
    public FirstFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_items;
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {

    }

    @Override
    protected void onStopUi() {

    }

    /*    private void updateState(UiState state) {

        if (this.state == state) {
            return;
        }
        this.state = state;

        switch (state) {
            case CONTENT:
                binding.stateful.showContent();
                break;
            case PROGRESS:
                binding.stateful.showProgress();
                break;
            case OFFLINE:
                binding.stateful.showOffline();
                break;
            case EMPTY:
                binding.stateful.showEmpty();
                break;
            case ERROR:
                binding.stateful.showEmpty();
                break;
        }
    }*/
}
