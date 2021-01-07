package com.example.pos.ui.pricing;

import android.content.Context;
import android.view.ViewGroup;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.SalesItem;
import com.example.pos.ui.base.GenericRecyclerViewAdapter;

import androidx.annotation.NonNull;

public class AddPriceItemAdapter extends GenericRecyclerViewAdapter<SalesItem, OnRecyclerItemClickListener, AddPriceItemViewHolder> {

    AddPriceItemAdapter(Context context, OnRecyclerItemClickListener listener) {
        super(context, listener);
    }

    @NonNull
    @Override
    public AddPriceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddPriceItemViewHolder(inflate(R.layout.add_price_item, parent), getListener());
    }
}
