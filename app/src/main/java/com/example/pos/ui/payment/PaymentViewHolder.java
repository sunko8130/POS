package com.example.pos.ui.payment;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.Payment;
import com.example.pos.ui.base.BaseViewHolder;
import com.example.pos.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentViewHolder extends BaseViewHolder<Payment, OnRecyclerItemClickListener> {

    private Context mContext;

    @BindView(R.id.tv_delivery_order_no)
    TextView tvDeliveryOrderNo;

    @BindView(R.id.tv_payment_no)
    TextView tvPaymentNo;

    @BindView(R.id.tv_payment_date)
    TextView tvPaymentDate;

    @BindView(R.id.tv_bal_to_paid)
    TextView tvBalanceToPaid;

    @BindView(R.id.tv_total_amount)
    TextView tvTotalAmount;

    @BindView(R.id.tv_payment_amount)
    TextView tvPaymentAmount;

    PaymentViewHolder(View itemView, OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(Payment payment) {
        tvDeliveryOrderNo.setText(payment.getDeliveryNo());
        tvPaymentNo.setText(payment.getPaymentNo());
        tvPaymentDate.setText(payment.getPaidDate());
        tvBalanceToPaid.setText(Html.fromHtml(mContext.getResources().getString(R.string.order_item_cost,
                Util.convertAmountWithSeparator(payment.getRemainAmount()))));
        tvTotalAmount.setText(Html.fromHtml(mContext.getResources().getString(R.string.order_item_cost,
                Util.convertAmountWithSeparator(payment.getTotalAmount()))));
        tvPaymentAmount.setText(Html.fromHtml(mContext.getResources().getString(R.string.order_item_cost,
                Util.convertAmountWithSeparator(payment.getCurrentPaidAmount()))));

    }
}
