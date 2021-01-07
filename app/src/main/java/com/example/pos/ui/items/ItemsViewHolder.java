package com.example.pos.ui.items;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.SearchItem;
import com.example.pos.ui.base.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemsViewHolder extends BaseViewHolder<SearchItem, OnRecyclerItemClickListener> {

    private Context mContext;

    @BindView(R.id.tv_main_category_name)
    TextView tvMainCategoryName;

    @BindView(R.id.tv_sub_category_name)
    TextView tvSubCategoryName;

    @BindView(R.id.tv_item_name)
    TextView tvItemName;

    @BindView(R.id.tv_stock_balance)
    TextView tvStockBalance;

    @BindView(R.id.tv_uom)
    TextView tvUOM;

    ItemsViewHolder(View itemView, OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(SearchItem searchItem) {
        tvMainCategoryName.setText(searchItem.getCategoryName());
        tvSubCategoryName.setText(searchItem.getSubCategoryName());
        tvItemName.setText(searchItem.getItemName());
        tvStockBalance.setText(String.valueOf(searchItem.getTotalBaseUomQty()));
        tvUOM.setText(searchItem.getUomName());
    }
}
