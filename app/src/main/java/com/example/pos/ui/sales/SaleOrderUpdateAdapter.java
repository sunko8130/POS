package com.example.pos.ui.sales;

import android.content.Context;
import android.view.ViewGroup;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerMultiItemClickListener;
import com.example.pos.model.SaleDetailsItem;
import com.example.pos.ui.base.GenericRecyclerViewAdapter;

import androidx.annotation.NonNull;

public class SaleOrderUpdateAdapter extends GenericRecyclerViewAdapter<SaleDetailsItem, OnRecyclerMultiItemClickListener, SaleOrderUpdateViewHolder> {

    SaleOrderUpdateAdapter(Context context, OnRecyclerMultiItemClickListener listener) {
        super(context, listener);
    }

    @NonNull
    @Override
    public SaleOrderUpdateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SaleOrderUpdateViewHolder(inflate(R.layout.update_sale_item, parent), getListener());
    }
}
