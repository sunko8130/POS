package com.example.pos.ui.payment;

import android.content.Context;
import android.view.ViewGroup;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.Payment;
import com.example.pos.ui.base.GenericRecyclerViewAdapter;

import androidx.annotation.NonNull;

public class PaymentAdapter extends GenericRecyclerViewAdapter<Payment, OnRecyclerItemClickListener, PaymentViewHolder> {

    PaymentAdapter(Context context, OnRecyclerItemClickListener listener) {
        super(context, listener);
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaymentViewHolder(inflate(R.layout.payment_item, parent), getListener());
    }
}
