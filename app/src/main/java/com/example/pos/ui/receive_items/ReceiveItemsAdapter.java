package com.example.pos.ui.receive_items;

import android.content.Context;
import android.view.ViewGroup;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.ReceiveItem;
import com.example.pos.ui.base.GenericRecyclerViewAdapter;

import androidx.annotation.NonNull;

public class ReceiveItemsAdapter extends GenericRecyclerViewAdapter<ReceiveItem, OnRecyclerItemClickListener, ReceiveItemsViewHolder> {

    ReceiveItemsAdapter(Context context, OnRecyclerItemClickListener listener) {
        super(context, listener);
    }

    @NonNull
    @Override
    public ReceiveItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReceiveItemsViewHolder(inflate(R.layout.receive_item, parent), getListener());
    }
}
