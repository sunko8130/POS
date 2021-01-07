package com.example.pos.ui.search_categories;

import android.content.Context;
import android.view.ViewGroup;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.ReceiveItems;
import com.example.pos.ui.base.GenericRecyclerViewAdapter;

import androidx.annotation.NonNull;

public class MainCategoryAdapter extends GenericRecyclerViewAdapter<ReceiveItems, OnRecyclerItemClickListener, MainCategoryViewHolder> {

    MainCategoryAdapter(Context context, OnRecyclerItemClickListener listener) {
        super(context, listener);
    }

    @NonNull
    @Override
    public MainCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainCategoryViewHolder(inflate(R.layout.main_category_item, parent), getListener());
    }
}
