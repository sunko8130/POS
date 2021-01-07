package com.example.pos.ui.pricing;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerMultiItemClickListener;
import com.example.pos.model.ItemPrice;
import com.example.pos.ui.base.BaseViewHolder;
import com.example.pos.util.Util;

import org.mmtextview.components.MMButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PricingViewHolder extends BaseViewHolder<ItemPrice, OnRecyclerMultiItemClickListener> {

    private Context mContext;

    @BindView(R.id.tv_item_code)
    TextView tvItemCode;

    @BindView(R.id.tv_item_name)
    TextView tvItemName;

    @BindView(R.id.tv_uom)
    TextView tvUOM;

    @BindView(R.id.et_price)
    EditText etPrice;

    @BindView(R.id.btn_add_price)
    MMButton btnAddPrice;


    PricingViewHolder(View itemView, OnRecyclerMultiItemClickListener listener) {
        super(itemView, listener);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(ItemPrice itemPrice) {
        tvItemCode.setText(itemPrice.getItemCode());
        tvItemName.setText(itemPrice.getItemName());
        tvUOM.setText(itemPrice.getUomName());
        etPrice.setText(Util.convertAmountWithSeparator(itemPrice.getUnitPrice()));
        btnAddPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = Integer.parseInt(etPrice.getText().toString().replace(",", ""));
                getListener().onClickAction(getAdapterPosition(), price);
                btnAddPrice.setEnabled(false);
                btnAddPrice.setBackgroundColor(mContext.getResources().getColor(R.color.md_grey_600));
            }
        });
    }
}
