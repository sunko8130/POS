package com.example.pos.ui.sales;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.SalesItem;
import com.example.pos.ui.base.BaseViewHolder;
import com.example.pos.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SalesItemViewHolder extends BaseViewHolder<SalesItem, OnRecyclerItemClickListener> {

    private Context mContext;

    @BindView(R.id.tv_sales_item_name)
    TextView tvItemName;

    @BindView(R.id.tv_sales_item_no_of_items)
    TextView tvNoOfItems;

    @BindView(R.id.tv_sales_item_uom)
    TextView tvUOM;

    @BindView(R.id.tv_sales_item_price)
    TextView tvPrice;

    @BindView(R.id.tv_sales_item_cost)
    TextView tvCost;

    @BindView(R.id.btn_delete)
    ImageButton btnDelete;

    SalesItemViewHolder(View itemView, OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
    }

    @Override
    public void onBind(SalesItem salesItem) {
        tvItemName.setText(salesItem.getItemName());
        tvNoOfItems.setText(salesItem.getQuantity());
        tvPrice.setText(Html.fromHtml(mContext.getResources().getString(R.string.order_item_cost,
                Util.convertAmountWithSeparator(salesItem.getPrice()))));
        tvUOM.setText(salesItem.getUom());
        tvCost.setText(Html.fromHtml(mContext.getResources().getString(R.string.order_item_cost,
                Util.convertAmountWithSeparator(salesItem.getAmount()))));
        btnDelete.setOnClickListener(v -> getListener().onItemClick(getAdapterPosition()));
    }
}
