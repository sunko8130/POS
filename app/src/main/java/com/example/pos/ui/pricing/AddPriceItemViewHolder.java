package com.example.pos.ui.pricing;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.SalesItem;
import com.example.pos.ui.base.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPriceItemViewHolder extends BaseViewHolder<SalesItem, OnRecyclerItemClickListener> {

    private Context mContext;

    @BindView(R.id.tv_item_name)
    TextView tvItemName;

    @BindView(R.id.tv_uom)
    TextView tvUOM;

    @BindView(R.id.tv_price)
    TextView tvPrice;

    AddPriceItemViewHolder(View itemView, OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
    }

    @Override
    public void onBind(SalesItem salesItem) {
        tvPrice.setText(String.valueOf(salesItem.getPrice()));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListener().onItemClick(getAdapterPosition());
            }
        });
    }
}
