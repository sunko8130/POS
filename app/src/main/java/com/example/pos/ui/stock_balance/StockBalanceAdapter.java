package com.example.pos.ui.stock_balance;

import android.content.Context;
import android.view.ViewGroup;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.StockBalance;
import com.example.pos.ui.base.GenericRecyclerViewAdapter;

import androidx.annotation.NonNull;

public class StockBalanceAdapter extends GenericRecyclerViewAdapter<StockBalance, OnRecyclerItemClickListener, StockBalanceViewHolder> {

    StockBalanceAdapter(Context context, OnRecyclerItemClickListener listener) {
        super(context, listener);
    }

    @NonNull
    @Override
    public StockBalanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StockBalanceViewHolder(inflate(R.layout.stock_item, parent), getListener());
    }
}
