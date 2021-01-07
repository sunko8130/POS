package com.example.pos.ui.receive_items;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.common.ViewModelFactory;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.DeliverNumbersResponse;
import com.example.pos.model.DeliveryOrdersResponse;
import com.example.pos.model.GenerateReceiveNoResponse;
import com.example.pos.model.LoginData;
import com.example.pos.model.NewReceiveSaveResponse;
import com.example.pos.model.OrderItem;
import com.example.pos.network.ApiResponse;
import com.example.pos.ui.base.BaseActivity;
import com.example.pos.util.Util;
import com.example.pos.widgets.MessageDialog;
import com.google.android.material.textfield.TextInputEditText;

import org.mmtextview.MMFontUtils;

import java.util.List;

import javax.inject.Inject;

import static com.example.pos.util.Constant.LOGIN_DATA;

public class AddNewReceiveItemActivity extends BaseActivity implements OnRecyclerItemClickListener, MessageDialog.OnClickListener {
    @BindView(R.id.rv_new_receive_items)
    RecyclerView rvNewReceiveItems;

    @BindView(R.id.et_receive_no)
    TextInputEditText etReceiveNo;

    @BindView(R.id.et_receiver_name)
    TextInputEditText etReceiverName;

    @BindView(R.id.spinner_delivery_number)
    AppCompatSpinner spinnerDeliveryNumber;

    @BindView(R.id.tv_no_items)
    TextView tvNoItems;

    @BindView(R.id.tv_total_amount)
    TextView tvTotalAmount;

    @BindView(R.id.tv_total_commission)
    TextView tvTotalCommission;

    @BindView(R.id.tv_total_tax)
    TextView tvTotalTax;

    @Inject
    ViewModelFactory viewModelFactory;

    private ReceiveItemsViewModel receiveItemsViewModel;
    private AddNewReceiveItemsAdapter addNewReceiveItemsAdapter;
    private LoginData loginData;
    private int merchantId;
    private String deliverNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_receive_item);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            if (MMFontUtils.isSupportUnicode(this)) {
                getSupportActionBar().setTitle(getString(R.string.add_receive_item));
            } else {
                getSupportActionBar().setTitle(Html.fromHtml(MMFontUtils.uni2zg(getString(R.string.add_receive_item))));
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        receiveItemsViewModel = ViewModelProviders.of(this, viewModelFactory).get(ReceiveItemsViewModel.class);

        //recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvNewReceiveItems.setLayoutManager(layoutManager);
        rvNewReceiveItems.setHasFixedSize(true);
        rvNewReceiveItems.setItemAnimator(new DefaultItemAnimator());
        rvNewReceiveItems.setNestedScrollingEnabled(false);
        addNewReceiveItemsAdapter = new AddNewReceiveItemsAdapter(this, this);

        //login data
        loginData = Paper.book().read(LOGIN_DATA);
        if (loginData != null) {
            merchantId = loginData.getId();
        }

        //observe generate receive no
        observeGenerateReceiveNo();
        receiveItemsViewModel.generateReceiveNo();

        //observe deliver numbers
        observeDeliverNumbers();

        //observe search
        observeDeliverOrdersSearch();

        //observe save
        observeSave();
    }

    private void observeSave() {
        receiveItemsViewModel.newReceiveSaveResponse.observe(this, this::consumeNewReceiveSave);
    }

    private void consumeNewReceiveSave(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                NewReceiveSaveResponse newReceiveSaveResponse = (NewReceiveSaveResponse) apiResponse.data;
                if (newReceiveSaveResponse != null) {
                    int statusCode = newReceiveSaveResponse.getStatus().getCode();
                    messageDialog.show();
                    if (statusCode == 1) {
                        messageDialog.loadingMessage(getString(R.string.success_message));
                        messageDialog.setListener(this);
                    } else {
                        messageDialog.loadingMessage(newReceiveSaveResponse.getStatus().getMessage());
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

    private void observeDeliverOrdersSearch() {
        receiveItemsViewModel.deliveryOrdersResponse.observe(this, this::consumeDeliverOrders);
    }

    private void consumeDeliverOrders(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                DeliveryOrdersResponse deliveryOrdersResponse = (DeliveryOrdersResponse) apiResponse.data;
                if (deliveryOrdersResponse != null) {
                    int statusCode = deliveryOrdersResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        List<OrderItem> orderItems = deliveryOrdersResponse.getData();
                        OrderItem orderItem = deliveryOrdersResponse.getOrder();
                        if (orderItems != null && orderItems.size() > 0) {
                            //show total amount
//                            double totalAmount = 0;
//                            for (OrderItem orderItem : orderItems) {
//                                totalAmount += orderItem.getTotal();
//                            }

                            if ((Util.convertAmountWithSeparator(orderItem.getComPer()).equals("0"))) {
                                tvTotalCommission.setVisibility(View.GONE);
                            } else {
                                tvTotalCommission.setVisibility(View.VISIBLE);
                            }
                            if ((Util.convertAmountWithSeparator(orderItem.getTaxPer()).equals("0"))) {
                                tvTotalTax.setVisibility(View.GONE);
                            } else {
                                tvTotalTax.setVisibility(View.VISIBLE);
                            }
                            String totalCommission = Util.convertAmountWithSeparator(orderItem.getComPer());
                            String totalTax = Util.convertAmountWithSeparator(orderItem.getTaxPer());
                            String totalPrice = Util.convertAmountWithSeparator(orderItem.getTotal());
                            tvTotalCommission.setText(Html.fromHtml(getResources().getString(R.string.total_commission, totalCommission)));
                            tvTotalTax.setText(Html.fromHtml(getResources().getString(R.string.receive_item_tax_label, totalTax)));
                            tvTotalAmount.setText(Html.fromHtml(getResources().getString(R.string.total_receive_order_price, totalPrice)));

                            rvNewReceiveItems.setAdapter(addNewReceiveItemsAdapter);
                            addNewReceiveItemsAdapter.setItems(orderItems);
                            tvNoItems.setVisibility(View.GONE);
                            tvTotalAmount.setVisibility(View.VISIBLE);
                        } else {
                            tvNoItems.setVisibility(View.VISIBLE);
                            tvTotalAmount.setVisibility(View.GONE);
                            tvTotalCommission.setVisibility(View.GONE);
                            tvTotalTax.setVisibility(View.GONE);
                        }
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(deliveryOrdersResponse.getStatus().getMessage());
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

    private void observeDeliverNumbers() {
        receiveItemsViewModel.deliveryNumbersResponse.observe(this, this::consumeDeliverNumbers);
    }

    private void consumeDeliverNumbers(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                DeliverNumbersResponse deliverNumbersResponse = (DeliverNumbersResponse) apiResponse.data;
                if (deliverNumbersResponse != null) {
                    int statusCode = deliverNumbersResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        List<String> deliverNumbersList = deliverNumbersResponse.getData();
                        if (deliverNumbersList != null && deliverNumbersList.size() > 0) {
                            ArrayAdapter<String> deliverNumbersAdapter = new ArrayAdapter<>(
                                    this, R.layout.spinner_item, R.id.spinnerText, deliverNumbersList);
                            spinnerDeliveryNumber.setAdapter(deliverNumbersAdapter);
                        } else {
                            messageDialog.show();
                            messageDialog.loadingMessage(getString(R.string.no_delivery_no_alert));
                            messageDialog.setListener(this);
                        }
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(deliverNumbersResponse.getStatus().getMessage());
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

    private void observeGenerateReceiveNo() {
        receiveItemsViewModel.generateReceiveNoResponse.observe(this, this::consumeGenerateReceiveNo);
    }

    private void consumeGenerateReceiveNo(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                receiveItemsViewModel.deliverNumbers(merchantId);
                GenerateReceiveNoResponse generateReceiveNoResponse = (GenerateReceiveNoResponse) apiResponse.data;
                if (generateReceiveNoResponse != null) {
                    int statusCode = generateReceiveNoResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        //generate receive no
                        String generateReceiveNo = generateReceiveNoResponse.getData();
                        etReceiveNo.setText(generateReceiveNo);
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(generateReceiveNoResponse.getStatus().getMessage());
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

    @OnClick(R.id.btn_search)
    void search() {
        tvTotalTax.setVisibility(View.GONE);
        tvTotalAmount.setVisibility(View.GONE);
        tvTotalCommission.setVisibility(View.GONE);
        rvNewReceiveItems.setAdapter(null);
        addNewReceiveItemsAdapter.clear();

        deliverNo = spinnerDeliveryNumber.getSelectedItem().toString();
        if (TextUtils.isEmpty(deliverNo) || deliverNo.equals("")) {
            Util.customToastMessage(AddNewReceiveItemActivity.this, getString(R.string.deliver_no_alert), false);
        } else {
            receiveItemsViewModel.deliveryOrders(deliverNo);
        }
    }

    @OnClick(R.id.btn_save)
    void newReceiveSave() {
        String receiveNo = etReceiveNo.getText().toString();
        String receiverName = etReceiverName.getText().toString();
        deliverNo = spinnerDeliveryNumber.getSelectedItem().toString();

        if (TextUtils.isEmpty(receiveNo)) {
            messageDialog.show();
            messageDialog.loadingMessage(getString(R.string.receive_no_alert));
        } else if (TextUtils.isEmpty(receiverName)) {
            messageDialog.show();
            messageDialog.loadingMessage(getString(R.string.receive_name_alert));
        } else if (TextUtils.isEmpty(deliverNo)) {
            messageDialog.show();
            messageDialog.loadingMessage(getString(R.string.deliver_no_alert));
        } else {
            receiveItemsViewModel.newReceiveSave(receiveNo, receiverName, deliverNo, merchantId);
        }

    }

    @OnClick(R.id.btn_cancel)
    void cancel() {
        Intent intent = new Intent(this, ReceiveItemsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onOkButtonClick() {
        Intent intent = new Intent(this, ReceiveItemsActivity.class);
        startActivity(intent);
        finish();
    }
}