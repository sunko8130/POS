package com.example.pos.ui.items;

import android.content.Context;
import android.view.ViewGroup;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.SearchItem;
import com.example.pos.ui.base.GenericRecyclerViewAdapter;

import androidx.annotation.NonNull;

public class ItemsAdapter extends GenericRecyclerViewAdapter<SearchItem, OnRecyclerItemClickListener, ItemsViewHolder> {

    ItemsAdapter(Context context, OnRecyclerItemClickListener listener) {
        super(context, listener);
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemsViewHolder(inflate(R.layout.items_item, parent), getListener());
    }
}
