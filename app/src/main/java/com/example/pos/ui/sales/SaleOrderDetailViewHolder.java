package com.example.pos.ui.sales;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.SaleDetailsItem;
import com.example.pos.ui.base.BaseViewHolder;
import com.example.pos.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SaleOrderDetailViewHolder extends BaseViewHolder<SaleDetailsItem, OnRecyclerItemClickListener> {

    private Context mContext;

    @BindView(R.id.tv_cost)
    TextView tvCost;

    @BindView(R.id.tv_uom)
    TextView tvUOM;

    @BindView(R.id.tv_price)
    TextView tvPrice;

    @BindView(R.id.tv_no_of_items)
    TextView tvNoOfItems;

    @BindView(R.id.tv_item_name)
    TextView tvItemNames;


    SaleOrderDetailViewHolder(View itemView, OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(SaleDetailsItem saleDetailsItem) {
        tvItemNames.setText(saleDetailsItem.getItemName());
        tvNoOfItems.setText(String.valueOf(saleDetailsItem.getQty()));
        tvUOM.setText(saleDetailsItem.getUom());
        tvPrice.setText(Html.fromHtml(mContext.getResources().getString(R.string.order_item_price,
                Util.convertAmountWithSeparator(saleDetailsItem.getUnitPrice()))));
        tvCost.setText(Html.fromHtml(mContext.getResources().getString(R.string.order_item_cost,
                Util.convertAmountWithSeparator(saleDetailsItem.getSubTotal()))));
    }
}
