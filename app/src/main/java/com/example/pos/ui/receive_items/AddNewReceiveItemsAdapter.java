package com.example.pos.ui.receive_items;

import android.content.Context;
import android.view.ViewGroup;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.OrderItem;
import com.example.pos.ui.base.GenericRecyclerViewAdapter;

import androidx.annotation.NonNull;

public class AddNewReceiveItemsAdapter extends GenericRecyclerViewAdapter<OrderItem, OnRecyclerItemClickListener, AddNewReceiveItemsViewHolder> {

    AddNewReceiveItemsAdapter(Context context, OnRecyclerItemClickListener listener) {
        super(context, listener);
    }

    @NonNull
    @Override
    public AddNewReceiveItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddNewReceiveItemsViewHolder(inflate(R.layout.new_receive_item, parent), getListener());
    }
}
