package com.example.pos.ui.sales;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.common.ViewModelFactory;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.delegate.OnRecyclerMultiItemClickListener;
import com.example.pos.model.DeleteSaleItemResponse;
import com.example.pos.model.LoginData;
import com.example.pos.model.SaleDetailsItem;
import com.example.pos.model.SaleOrder;
import com.example.pos.model.SaleOrderDetail;
import com.example.pos.model.SaleOrderDetailResponse;
import com.example.pos.model.UpdateSaleItemResponse;
import com.example.pos.network.ApiResponse;
import com.example.pos.ui.base.BaseDialogFragment;
import com.example.pos.util.Util;
import com.example.pos.widgets.ConfirmDialog;
import com.example.pos.widgets.MessageDialog;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

import static com.example.pos.util.Constant.LOGIN_DATA;

public class SaleItemUpdateFragment extends BaseDialogFragment implements OnRecyclerItemClickListener, MessageDialog.OnClickListener, OnRecyclerMultiItemClickListener {

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
    private LoginData loginData;
    private int merchantId;
    List<SaleDetailsItem> saleDetailsItems;
    private ReloadListener listener;

    @Inject
    ViewModelFactory viewModelFactory;
    private SaleOrderUpdateAdapter saleOrderUpdateAdapter;
    private SaleDetailsItem saleDetailsItem;
    private boolean isLastItem;
    private ConfirmDialog warningDialog;

    public static SaleItemUpdateFragment newInstance(SaleOrder saleOrder) {
        SaleItemUpdateFragment fragment = new SaleItemUpdateFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, saleOrder);
        fragment.setArguments(args);
        return fragment;
    }

    public SaleItemUpdateFragment() {
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
        saleOrderUpdateAdapter = new SaleOrderUpdateAdapter(mContext, this);
        rvSaleOrders.setAdapter(saleOrderUpdateAdapter);

        warningDialog = new ConfirmDialog(mContext);

        //login data
        loginData = Paper.book().read(LOGIN_DATA);
        if (loginData != null) {
            merchantId = loginData.getId();
        }

        salesViewModel.saleOrderDetail(saleOrder.getSalesNo(), saleOrder.getSalesAmount());
        observeSaleOrderDetail();

        observeDeleteSaleItem();

        observeUpdateSaleItem();

        btnDelete.setOnClickListener(v -> {
            listener.reload();
            dismiss();
        });
    }

    private void observeUpdateSaleItem() {
        salesViewModel.updateSaleItemResponse.observe(this, this::consumeUpdateSaleItem);
    }

    private void consumeUpdateSaleItem(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                UpdateSaleItemResponse updateSaleItemResponse = (UpdateSaleItemResponse) apiResponse.data;
                if (updateSaleItemResponse != null) {
                    int statusCode = updateSaleItemResponse.getStatus().getCode();
                    String updateBalance = updateSaleItemResponse.getBalance();
                    if (statusCode == 1) {
                        messageDialog.show();
                        messageDialog.loadingMessage(getString(R.string.success));
                        messageDialog.setListener(new MessageDialog.OnClickListener() {
                            @Override
                            public void onOkButtonClick() {
                                messageDialog.dismiss();
                                salesViewModel.saleOrderDetail(saleOrder.getSalesNo(), Double.parseDouble(updateBalance));
                            }
                        });
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

    private void observeDeleteSaleItem() {
        salesViewModel.deleteSaleItemResponse.observe(this, this::consumeDeleteSaleItem);
    }

    private void consumeDeleteSaleItem(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                DeleteSaleItemResponse deleteSaleItemResponse = (DeleteSaleItemResponse) apiResponse.data;
                if (deleteSaleItemResponse != null) {
                    int statusCode = deleteSaleItemResponse.getStatus().getCode();
                    String updateBalance = deleteSaleItemResponse.getBalance();
                    if (statusCode == 1) {
                        if (isLastItem) {
                            saleDetailsItems.remove(saleDetailsItem);
                            saleOrderUpdateAdapter.remove(saleDetailsItem);
                            messageDialog.show();
                            messageDialog.loadingMessage(getString(R.string.success));
                            messageDialog.setListener(this);
                        } else {
                            saleDetailsItems.remove(saleDetailsItem);
                            saleOrderUpdateAdapter.remove(saleDetailsItem);
                            messageDialog.show();
                            messageDialog.loadingMessage(getString(R.string.success));
                            messageDialog.setListener(new MessageDialog.OnClickListener() {
                                @Override
                                public void onOkButtonClick() {
                                    messageDialog.dismiss();
                                    salesViewModel.saleOrderDetail(saleOrder.getSalesNo(), Double.parseDouble(updateBalance));
                                }
                            });
                        }
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
                            saleDetailsItems = saleOrderDetail.getDetails();
                            if (saleDetailsItems != null && saleDetailsItems.size() > 0) {
                                rvSaleOrders.setAdapter(saleOrderUpdateAdapter);
                                saleOrderUpdateAdapter.setItems(saleDetailsItems);
                            }
                        }
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(saleOrderDetailResponse.getStatus().getMessage());
                        messageDialog.setListener(this);
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
        saleDetailsItem = saleOrderUpdateAdapter.getItem(position);
        if (isLastVisible()) {
            int isLast = 1;
            isLastItem = true;
            salesViewModel.deleteSaleItem(saleDetailsItem.getId(), saleDetailsItem.getItemCode(), saleDetailsItem.getUomId(), merchantId, saleDetailsItem.getQty(), saleOrder.getSalesNo(), isLast);
        } else {
            int isLast = 0;
            isLastItem = false;
            rvSaleOrders.setAdapter(null);
            saleOrderUpdateAdapter.clear();
            salesViewModel.deleteSaleItem(saleDetailsItem.getId(), saleDetailsItem.getItemCode(), saleDetailsItem.getUomId(), merchantId, saleDetailsItem.getQty(), saleOrder.getSalesNo(), isLast);
        }
    }

    @Override
    public void onClickAction(int pos, int value) {
        saleDetailsItem = saleOrderUpdateAdapter.getItem(pos);
        warningDialog.show();
        warningDialog.loadingMessage(getString(R.string.warning_msg_update_qty));
        warningDialog.setListener(() -> {
//            rvSaleOrders.setAdapter(null);
//            saleOrderUpdateAdapter.clear();
            salesViewModel.updateSaleItem(saleDetailsItem.getId(), saleDetailsItem.getItemCode(), saleDetailsItem.getUomId(), merchantId, value, saleOrder.getSalesNo());
            warningDialog.dismiss();
        });
    }

    boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) rvSaleOrders.getLayoutManager());
        int pos = layoutManager.findFirstVisibleItemPosition();
        int numItems = saleOrderUpdateAdapter.getItemCount() - 1;
        return (pos >= numItems);
    }

    @Override
    public void onOkButtonClick() {
        messageDialog.dismiss();
        dismiss();
        listener.reload();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ReloadListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ReloadListener");
        }
    }

    public interface ReloadListener {
        void reload();
    }
}