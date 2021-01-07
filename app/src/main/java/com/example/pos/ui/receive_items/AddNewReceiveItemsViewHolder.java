package com.example.pos.ui.receive_items;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.OrderItem;
import com.example.pos.ui.base.BaseViewHolder;
import com.example.pos.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewReceiveItemsViewHolder extends BaseViewHolder<OrderItem, OnRecyclerItemClickListener> {

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

    @BindView(R.id.tv_commission)
    TextView tvCommission;

    @BindView(R.id.commission_table_row)
    TableRow commissionTableRow;


    AddNewReceiveItemsViewHolder(View itemView, OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(OrderItem orderItem) {
        if ((Util.convertAmountWithSeparator(orderItem.getComPer()).equals("0"))) {
            commissionTableRow.setVisibility(View.GONE);
        } else {
            commissionTableRow.setVisibility(View.VISIBLE);
        }
        tvCommission.setText(Html.fromHtml(mContext.getResources().getString(R.string.order_item_commission,
                Util.convertAmountWithSeparator(orderItem.getComPer()))));
        tvItemNames.setText(orderItem.getItem());
        tvNoOfItems.setText(String.valueOf(orderItem.getQty()));
        tvUOM.setText(orderItem.getUom());
        tvPrice.setText(Html.fromHtml(mContext.getResources().getString(R.string.order_item_price,
                Util.convertAmountWithSeparator(orderItem.getPrice()))));
        tvCost.setText(Html.fromHtml(mContext.getResources().getString(R.string.order_item_cost,
                Util.convertAmountWithSeparator(orderItem.getTotal()))));
    }
}
