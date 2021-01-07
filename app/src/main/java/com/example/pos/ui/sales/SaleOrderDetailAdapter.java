package com.example.pos.ui.sales;

import android.content.Context;
import android.view.ViewGroup;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.SaleDetailsItem;
import com.example.pos.ui.base.GenericRecyclerViewAdapter;

import androidx.annotation.NonNull;

public class SaleOrderDetailAdapter extends GenericRecyclerViewAdapter<SaleDetailsItem, OnRecyclerItemClickListener, SaleOrderDetailViewHolder> {

    SaleOrderDetailAdapter(Context context, OnRecyclerItemClickListener listener) {
        super(context, listener);
    }

    @NonNull
    @Override
    public SaleOrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SaleOrderDetailViewHolder(inflate(R.layout.new_receive_item, parent), getListener());
    }
}
