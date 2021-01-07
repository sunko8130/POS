package com.example.pos.ui.search_categories;

import android.content.Context;
import android.view.View;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.ReceiveItems;
import com.example.pos.ui.base.BaseViewHolder;

import org.mmtextview.components.MMTextView;

import butterknife.BindView;

public class MainCategoryViewHolder extends BaseViewHolder<ReceiveItems, OnRecyclerItemClickListener> {

    private Context mContext;

    @BindView(R.id.tv_main_category_name)
    MMTextView tvMainCategoryName;

    @BindView(R.id.tv_no_of_items)
    MMTextView tvNoOfItems;

    MainCategoryViewHolder(View itemView, OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        mContext = itemView.getContext();
    }

    @Override
    public void onBind(ReceiveItems receiveItems) {
    }
}
