package com.example.pos.ui.pricing;

import android.content.Context;
import android.view.ViewGroup;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerMultiItemClickListener;
import com.example.pos.model.ItemPrice;
import com.example.pos.ui.base.GenericRecyclerViewAdapter;
import androidx.annotation.NonNull;

public class PricingAdapter extends GenericRecyclerViewAdapter<ItemPrice, OnRecyclerMultiItemClickListener, PricingViewHolder> {

    PricingAdapter(Context context, OnRecyclerMultiItemClickListener listener) {
        super(context, listener);
    }

    @NonNull
    @Override
    public PricingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PricingViewHolder(inflate(R.layout.pricing_item, parent), getListener());
    }
}
