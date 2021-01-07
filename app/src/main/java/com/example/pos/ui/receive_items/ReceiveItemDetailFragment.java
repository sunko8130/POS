package com.example.pos.ui.receive_items;

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
import android.widget.TableRow;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.common.ViewModelFactory;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.OrderItem;
import com.example.pos.model.ReceiveItem;
import com.example.pos.model.ReceiveItemView;
import com.example.pos.model.ReceiveItemViewResponse;
import com.example.pos.network.ApiResponse;
import com.example.pos.ui.base.BaseDialogFragment;
import com.example.pos.util.Util;

import java.util.List;

import javax.inject.Inject;

public class ReceiveItemDetailFragment extends BaseDialogFragment implements OnRecyclerItemClickListener {

    @BindView(R.id.tv_receive_date)
    TextView tvReceiveDate;

    @BindView(R.id.tv_receiver_name)
    TextView tvReceiveName;

    @BindView(R.id.tv_amount)
    TextView tvAmount;

    @BindView(R.id.tv_receive_no)
    TextView tvReceiveNumber;

    @BindView(R.id.tv_commission)
    TextView tvCommission;

    @BindView(R.id.tv_tax)
    TextView tvTax;

    @BindView(R.id.tv_commission_label)
    TextView tvCommissionLabel;

    @BindView(R.id.tv_tax_label)
    TextView tvTaxLabel;

    @BindView(R.id.commission_row)
    TableRow commissionRow;

    @BindView(R.id.tax_row)
    TableRow taxRow;

    @BindView(R.id.btn_delete)
    ImageButton btnDelete;

    @BindView(R.id.rv_new_receive_items)
    RecyclerView rvItems;

    @Inject
    ViewModelFactory viewModelFactory;

    private static final String ARG_PARAM1 = "param1";
    private AddNewReceiveItemsAdapter addNewReceiveItemsAdapter;
    private ReceiveItem receiveItem;
    private View rootView;
    private ReceiveItemsViewModel receiveItemsViewModel;

    public ReceiveItemDetailFragment() {
        // Required empty public constructor
    }

    public static ReceiveItemDetailFragment newInstance(ReceiveItem receiveItem) {
        ReceiveItemDetailFragment fragment = new ReceiveItemDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, receiveItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        if (getArguments() != null) {
            receiveItem = (ReceiveItem) getArguments().getSerializable(ARG_PARAM1);
        }
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViews();
        return rootView;
    }

    private void initViews() {
        rootView = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_receive_item_datail, null, false);
        ButterKnife.bind(this, rootView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        receiveItemsViewModel = ViewModelProviders.of(this, viewModelFactory).get(ReceiveItemsViewModel.class);

        //init rv
        rvItems.setLayoutManager(new LinearLayoutManager(mContext));
        rvItems.setNestedScrollingEnabled(false);
        addNewReceiveItemsAdapter = new AddNewReceiveItemsAdapter(mContext, this);
        rvItems.setAdapter(addNewReceiveItemsAdapter);
        receiveItemsViewModel.receiveItemView(receiveItem.getReceiveOrderNo(), receiveItem.getReceivedPersonName());
        observeReceiveItemView();

        btnDelete.setOnClickListener(v -> dismiss());
    }

    private void observeReceiveItemView() {
        receiveItemsViewModel.receiveItemViewResponse.observe(this, this::consumeReceiveItemView);
    }

    private void consumeReceiveItemView(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                ReceiveItemViewResponse receiveItemViewResponse = (ReceiveItemViewResponse) apiResponse.data;
                if (receiveItemViewResponse != null) {
                    int statusCode = receiveItemViewResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        ReceiveItemView receiveItemView = receiveItemViewResponse.getData();
                        if (receiveItemView != null) {
                            tvReceiveNumber.setText(receiveItemView.getReceiveOrderNo());
                            tvReceiveName.setText(receiveItemView.getReceivePersonName());
                            tvReceiveDate.setText(receiveItem.getCreatedDate());
                            tvAmount.setText(Html.fromHtml(mContext.getResources().getString(R.string.order_item_cost,
                                    Util.convertAmountWithSeparator(receiveItemView.getTotalAmount()))));
                            if ((Util.convertAmountWithSeparator(receiveItemView.getCommPer()).equals("0"))) {
                                commissionRow.setVisibility(View.GONE);
                            } else {
                                commissionRow.setVisibility(View.VISIBLE);
                                tvCommissionLabel.setText(Html.fromHtml(mContext.getResources().getString(R.string.receive_item_commission_label,
                                        Util.convertAmountWithSeparator(receiveItemView.getCommPer()))));
                                tvCommission.setText(Html.fromHtml(mContext.getResources().getString(R.string.order_item_cost,
                                        Util.convertAmountWithSeparator(receiveItemView.getCommAmount()))));
                            }
                            if ((Util.convertAmountWithSeparator(receiveItemView.getTaxPer()).equals("0"))) {
                                taxRow.setVisibility(View.GONE);
                            } else {
                                taxRow.setVisibility(View.VISIBLE);
                                tvTaxLabel.setText(Html.fromHtml(mContext.getResources().getString(R.string.receive_item_tax_label,
                                        Util.convertAmountWithSeparator(receiveItemView.getTaxPer()))));
                                tvTax.setText(Html.fromHtml(mContext.getResources().getString(R.string.order_item_cost,
                                        Util.convertAmountWithSeparator(receiveItemView.getTaxAmount()))));

                            }

                            List<OrderItem> orderItems = receiveItemView.getResList();
                            if (orderItems != null && orderItems.size() > 0) {
                                rvItems.setAdapter(addNewReceiveItemsAdapter);
                                addNewReceiveItemsAdapter.setItems(orderItems);
                            }
                        }
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(receiveItemViewResponse.getStatus().getMessage());
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