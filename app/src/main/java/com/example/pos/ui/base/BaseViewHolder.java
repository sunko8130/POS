package com.example.pos.ui.base;

import android.view.View;

import com.example.pos.delegate.BaseRecyclerListener;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder<T, L extends BaseRecyclerListener> extends RecyclerView.ViewHolder {

    private L mListener;

    public BaseViewHolder(View itemView, L listener) {
        super(itemView);
        this.mListener = listener;
    }

    protected abstract void onBind(T item);

    public L getListener() {
        return mListener;
    }
}