package com.example.pos.ui.sales;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.common.ViewModelFactory;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.SaleDetailsItem;
import com.example.pos.model.SaleOrder;
import com.example.pos.model.SaleOrderDetail;
import com.example.pos.model.SaleOrderDetailResponse;
import com.example.pos.network.ApiResponse;
import com.example.pos.ui.base.BaseDialogFragment;
import com.example.pos.util.Util;

import java.util.List;

import javax.inject.Inject;

public class SaleItemDetailFragment extends BaseDialogFragment implements OnRecyclerItemClickListener {

    @BindView(R.id.tv_sale_order_number)
    TextView tvSaleOderNumber;

    @BindView(R.id.tv_sales_amount)
    TextView tvSaleAmount;

    @BindView(R.id.rv_sale_orders)
    RecyclerView rvSaleOrders;

    @BindView(R.id.btn_delete)
    ImageButton btnDelete;

    private static final String ARG_PARAM1 = "param1";

    private SaleOrder saleOrder;
    private View rootView;
    private SalesViewModel salesViewModel;

    @Inject
    ViewModelFactory viewModelFactory;
    private SaleOrderDetailAdapter saleOrderDetailAdapter;

    public static SaleItemDetailFragment newInstance(SaleOrder saleOrder) {
        SaleItemDetailFragment fragment = new SaleItemDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, saleOrder);
        fragment.setArguments(args);
        return fragment;
    }

    public SaleItemDetailFragment() {
        // Required empty public constructor
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        initViews();
//        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
//                .setView(rootView)
//                .setCancelable(false)
//                .create();
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.setCancelable(false);
//        return alertDialog;
//    }

    private void initViews() {
        rootView = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_sale_item_detail, null, false);
        ButterKnife.bind(this, rootView);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        if (getArguments() != null) {
            saleOrder = (SaleOrder) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViews();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        salesViewModel = ViewModelProviders.of(this, viewModelFactory).get(SalesViewModel.class);

        //init rv
        rvSaleOrders.setLayoutManager(new LinearLayoutManager(mContext));
        rvSaleOrders.setNestedScrollingEnabled(false);
        saleOrderDetailAdapter = new SaleOrderDetailAdapter(mContext, this);
        rvSaleOrders.setAdapter(saleOrderDetailAdapter);

        salesViewModel.saleOrderDetail(saleOrder.getSalesNo(), saleOrder.getSalesAmount());
        observeSaleOrderDetail();

        btnDelete.setOnClickListener(v -> dismiss());
    }

    private void observeSaleOrderDetail() {
        salesViewModel.saleOrderDetailResponse.observe(this, this::consumeSaleOrderDetail);
    }

    private void consumeSaleOrderDetail(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                SaleOrderDetailResponse saleOrderDetailResponse = (SaleOrderDetailResponse) apiResponse.data;
                if (saleOrderDetailResponse != null) {
                    int statusCode = saleOrderDetailResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        SaleOrderDetail saleOrderDetail = saleOrderDetailResponse.getData();
                        if (saleOrderDetail != null) {
                            tvSaleOderNumber.setText(saleOrderDetail.getSalesNo());
                            tvSaleAmount.setText(Html.fromHtml(mContext.getResources().getString(R.string.order_item_price,
                                    Util.convertAmountWithSeparator(saleOrderDetail.getTotal()))));
                            List<SaleDetailsItem> saleDetailsItems = saleOrderDetail.getDetails();
                            if (saleDetailsItems != null && saleDetailsItems.size() > 0) {
                                rvSaleOrders.setAdapter(saleOrderDetailAdapter);
                                saleOrderDetailAdapter.setItems(saleDetailsItems);
                            }
                        }
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(saleOrderDetailResponse.getStatus().getMessage());
                    }
                }
                break;
            case ERROR:
                loadingDialog.dismiss();
                if (apiResponse.message != null) {
                    messageDialog.show();
                    messageDialog.loadingMessage(apiResponse.message);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(int position) {

    }
}