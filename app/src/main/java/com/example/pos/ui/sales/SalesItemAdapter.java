package com.example.pos.ui.sales;

import android.content.Context;
import android.view.ViewGroup;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.SalesItem;
import com.example.pos.ui.base.GenericRecyclerViewAdapter;

import androidx.annotation.NonNull;

public class SalesItemAdapter extends GenericRecyclerViewAdapter<SalesItem, OnRecyclerItemClickListener, SalesItemViewHolder> {

    SalesItemAdapter(Context context, OnRecyclerItemClickListener listener) {
        super(context, listener);
    }

    @NonNull
    @Override
    public SalesItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SalesItemViewHolder(inflate(R.layout.sales_item_layout, parent), getListener());
    }
}
