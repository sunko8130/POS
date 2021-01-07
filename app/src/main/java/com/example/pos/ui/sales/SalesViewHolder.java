package com.example.pos.ui.sales;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.delegate.OnRecyclerMultiItemClickListener;
import com.example.pos.model.SaleOrder;
import com.example.pos.ui.base.BaseViewHolder;
import com.example.pos.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SalesViewHolder extends BaseViewHolder<SaleOrder, OnRecyclerMultiItemClickListener> {

    private Context mContext;

    @BindView(R.id.tv_sales_order_number)
    TextView tvSalesOrderNumber;

    @BindView(R.id.tv_sales_order_date)
    TextView tvSalesOrderDate;

    @BindView(R.id.tv_sales_amount)
    TextView tvSalesAmount;

    @BindView(R.id.btn_view_detail)
    Button btnViewDetail;

    @BindView(R.id.btn_update)
    Button btnUpdate;

    SalesViewHolder(View itemView, OnRecyclerMultiItemClickListener listener) {
        super(itemView, listener);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(SaleOrder saleOrder) {

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Calendar today = Calendar.getInstance();
        //current_CalendarDay.setTime(today);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        Date dtToday = today.getTime();

        tvSalesOrderNumber.setText(saleOrder.getSalesNo());
        tvSalesOrderDate.setText(saleOrder.getSalesDate());
        tvSalesAmount.setText(Html.fromHtml(mContext.getResources().getString(R.string.order_item_cost,
                Util.convertAmountWithSeparator(saleOrder.getSalesAmount()))));
        btnViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListener().onItemClick(getAdapterPosition());
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Date saleDate = sdf.parse(saleOrder.getSalesDate());
                    if (saleDate.equals(dtToday)) {
                        getListener().onClickAction(getAdapterPosition(), getAdapterPosition());
                        btnUpdate.setBackgroundColor(mContext.getResources().getColor(R.color.md_blue_grey_700));
                    } else {
                        btnUpdate.setBackgroundColor(mContext.getResources().getColor(R.color.md_grey_600));
                        Util.customToastMessage(mContext, mContext.getString(R.string.surrent_date_sale_update), false);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
