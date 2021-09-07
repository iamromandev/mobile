/*
package com.dreampany.frame.ui.model;

import android.content.Context;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.google.common.base.Objects;

import java.io.Serializable;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.viewholders.FlexibleViewHolder;

*/
/**
 * Created by Hawladar Roman on 30/4/18.
 * Dreampany
 * dreampanymail@gmail.com
 *//*

public abstract class BaseItem<T, VH extends BaseItem.ViewHolder> extends AbstractFlexibleItem<VH> implements IFilterable, Serializable {

    protected T item;
    @LayoutRes
    protected int layoutId;
    protected boolean success;

    protected BaseItem(T item, @LayoutRes int layoutId) {
        this.item = item;
        this.layoutId = layoutId;
        this.success = true;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        BaseItem item = (BaseItem) other;
        return Objects.equal(this.item, item.item);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(item);
    }

    public void setItem(T item) {
        this.item = item;
    }

    public void setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @LayoutRes
    @Override
    public int getLayoutRes() {
        return layoutId;
    }

    @NonNull
    public T getItem() {
        return item;
    }

    public boolean isSuccess() {
        return success;
    }


    public static abstract class ViewHolder extends FlexibleViewHolder {

        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
        }

        public Context getContext() {
            return itemView.getContext();
        }
    }
}
*/
