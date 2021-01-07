package com.example.pos.ui.category;

import android.content.Context;
import android.view.ViewGroup;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.ReceiveItems;
import com.example.pos.ui.base.GenericRecyclerViewAdapter;

import androidx.annotation.NonNull;

public class CategorySearchAdapter extends GenericRecyclerViewAdapter<ReceiveItems, OnRecyclerItemClickListener, CategorySearchViewHolder> {

    CategorySearchAdapter(Context context, OnRecyclerItemClickListener listener) {
        super(context, listener);
    }

    @NonNull
    @Override
    public CategorySearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategorySearchViewHolder(inflate(R.layout.sub_category_item, parent), getListener());
    }
}
