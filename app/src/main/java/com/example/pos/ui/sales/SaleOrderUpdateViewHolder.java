package com.example.pos.ui.sales;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerMultiItemClickListener;
import com.example.pos.model.SaleDetailsItem;
import com.example.pos.ui.base.BaseViewHolder;
import com.example.pos.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SaleOrderUpdateViewHolder extends BaseViewHolder<SaleDetailsItem, OnRecyclerMultiItemClickListener> {

    private Context mContext;

    @BindView(R.id.tv_cost)
    TextView tvCost;

    @BindView(R.id.tv_uom)
    TextView tvUOM;

    @BindView(R.id.tv_price)
    TextView tvPrice;

    @BindView(R.id.tv_no_of_items)
    EditText tvNoOfItems;

    @BindView(R.id.tv_item_name)
    TextView tvItemNames;

    @BindView(R.id.btn_delete)
    ImageButton btnDelete;

    @BindView(R.id.btn_update_qty)
    Button btnUpdateQty;


    SaleOrderUpdateViewHolder(View itemView, OnRecyclerMultiItemClickListener listener) {
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
        btnDelete.setOnClickListener(v -> getListener().onItemClick(getAdapterPosition()));
        btnUpdateQty.setOnClickListener(v -> {
            String qty = tvNoOfItems.getText().toString();
            double totalCost = Double.parseDouble(qty) * saleDetailsItem.getUnitPrice();
            tvCost.setText(Html.fromHtml(mContext.getResources().getString(R.string.order_item_cost,
                    Util.convertAmountWithSeparator(totalCost))));
            getListener().onClickAction(getAdapterPosition(), Integer.parseInt(qty));
        });
    }
}
