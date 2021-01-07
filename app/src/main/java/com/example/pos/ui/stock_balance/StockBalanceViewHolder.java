package com.example.pos.ui.stock_balance;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.StockBalance;
import com.example.pos.ui.base.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockBalanceViewHolder extends BaseViewHolder<StockBalance, OnRecyclerItemClickListener> {

    private Context mContext;

    @BindView(R.id.tv_item_code)
    TextView tvItemCode;

    @BindView(R.id.tv_item_name)
    TextView tvItemName;

    @BindView(R.id.tv_total_in)
    TextView tvTotalIn;

    @BindView(R.id.tv_total_out)
    TextView tvTotalOut;

    @BindView(R.id.tv_balance)
    TextView tvBalance;

    @BindView(R.id.tv_uom)
    TextView tvUOM;


    StockBalanceViewHolder(View itemView, OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(StockBalance stockBalance) {
        tvItemCode.setText(stockBalance.getItemCode());
        tvItemName.setText(stockBalance.getItemName());
        tvUOM.setText(stockBalance.getUomName());
        tvTotalIn.setText(String.valueOf(stockBalance.getReceiveText()));
        tvTotalOut.setText(String.valueOf(stockBalance.getSaleQty() + " " + stockBalance.getUomName()));
        tvBalance.setText(String.valueOf(stockBalance.getBalText()));
    }
}
