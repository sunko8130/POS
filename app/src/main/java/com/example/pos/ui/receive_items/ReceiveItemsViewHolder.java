package com.example.pos.ui.receive_items;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.ReceiveItem;
import com.example.pos.ui.base.BaseViewHolder;
import com.example.pos.util.Util;

import org.mmtextview.components.MMButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReceiveItemsViewHolder extends BaseViewHolder<ReceiveItem, OnRecyclerItemClickListener> {

    private Context mContext;

    @BindView(R.id.tv_receive_date)
    TextView tvReceiveDate;
    @BindView(R.id.tv_receiver_name)
    TextView tvReceiveName;
    @BindView(R.id.tv_amount)
    TextView tvAmount;

    @BindView(R.id.btn_view_detail)
    MMButton btnViewDetail;

    ReceiveItemsViewHolder(View itemView, OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(ReceiveItem receiveItem) {
        tvReceiveDate.setText(receiveItem.getCreatedDate());
        tvReceiveName.setText(receiveItem.getReceivedPersonName());
        tvAmount.setText(Html.fromHtml(mContext.getResources().getString(R.string.order_item_cost,
                Util.convertAmountWithSeparator(receiveItem.getTotalAmount()))));
        btnViewDetail.setOnClickListener(v -> getListener().onItemClick(getAdapterPosition()));
    }
}
