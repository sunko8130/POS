package com.example.pos.ui.sales;

import android.content.Context;
import android.view.ViewGroup;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerMultiItemClickListener;
import com.example.pos.model.SaleOrder;
import com.example.pos.ui.base.GenericRecyclerViewAdapter;

import androidx.annotation.NonNull;

public class SalesAdapter extends GenericRecyclerViewAdapter<SaleOrder, OnRecyclerMultiItemClickListener, SalesViewHolder> {

    SalesAdapter(Context context, OnRecyclerMultiItemClickListener listener) {
        super(context, listener);
    }

    @NonNull
    @Override
    public SalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SalesViewHolder(inflate(R.layout.sales_item, parent), getListener());
    }
}
