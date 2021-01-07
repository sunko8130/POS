package com.example.pos.ui.sales;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.pos.R;
import com.example.pos.common.ViewModelFactory;
import com.example.pos.model.LoginData;
import com.example.pos.model.MerchantItem;
import com.example.pos.model.MerchantItemsResponse;
import com.example.pos.model.PreSaleResponse;
import com.example.pos.model.UOM;
import com.example.pos.model.UOMResponse;
import com.example.pos.network.ApiResponse;
import com.example.pos.ui.base.BaseActivity;
import com.example.pos.ui.pricing.MerchantItemsAdapter;
import com.example.pos.ui.pricing.PricingViewModel;
import com.example.pos.util.Util;
import com.example.pos.widgets.MMEditText;
import com.example.pos.widgets.MessageDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import javax.inject.Inject;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

import static com.example.pos.util.Constant.LOGIN_DATA;

public class SalesOrdersActivity extends BaseActivity implements MessageDialog.OnClickListener {

    @BindView(R.id.spinner_item_name)
    AppCompatSpinner spinnerItemName;

    @BindView(R.id.et_quantity)
    MMEditText etQty;

    @BindView(R.id.spinner_uom)
    AppCompatSpinner spinnerUOM;

    @BindView(R.id.et_price)
    TextInputEditText etPrice;

    @BindView(R.id.et_amount)
    TextInputEditText etAmount;

    @Inject
    ViewModelFactory viewModelFactory;
    private SalesViewModel salesViewModel;
    private PricingViewModel pricingViewModel;
    private LoginData loginData;
    private int merchantId;
    private MerchantItemsAdapter itemsAdapter;
    private UOMAdapter uomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_orders);
        ButterKnife.bind(this);

        //back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        salesViewModel = ViewModelProviders.of(this, viewModelFactory).get(SalesViewModel.class);
        pricingViewModel = ViewModelProviders.of(this, viewModelFactory).get(PricingViewModel.class);

        //login data
        loginData = Paper.book().read(LOGIN_DATA);
        if (loginData != null) {
            merchantId = loginData.getId();
        }

        //observe merchant items
        pricingViewModel.merchantItems(merchantId);
        observeItems();
        //observe uom
        observeUOM();

        //observe pre sale
        observePreSale();


    }

    private void observePreSale() {
        salesViewModel.preSaleResponse.observe(this, this::consumePreSale);
    }

    private void consumePreSale(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                PreSaleResponse preSaleResponse = (PreSaleResponse) apiResponse.data;
                if (preSaleResponse != null) {
                    int statusCode = preSaleResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        String qty = etQty.getText().toString();
                        String price = etPrice.getText().toString();
                        String amount = etAmount.getText().toString();
                        MerchantItem merchantItem = (MerchantItem) spinnerItemName.getSelectedItem();
                        UOM uom = (UOM) spinnerUOM.getSelectedItem();
//                        SalesItem salesItem = new SalesItem(merchantItem.getItemName(), uom.getUomName(), price, amount, qty);
//                        salesItemList.add(salesItem);
//                        salesItemAdapter.add(salesItem);
//                        Log.e("sale amount", salesItem.getAmount());
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(getString(R.string.not_enough_quantity));
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

    private void observeItems() {
        pricingViewModel.merchantItemsResponse.observe(this, this::consumeItems);
    }

    private void consumeItems(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                MerchantItemsResponse itemsResponse = (MerchantItemsResponse) apiResponse.data;
                if (itemsResponse != null) {
                    int statusCode = itemsResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        List<MerchantItem> items = itemsResponse.getData();
                        if (items != null && items.size() > 0) {
                            itemsAdapter = new MerchantItemsAdapter(
                                    this, R.layout.spinner_item, R.id.spinnerText, items);
                            spinnerItemName.setAdapter(itemsAdapter);
                            spinnerItemName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    //item search
                                    MerchantItem merchantItem = itemsAdapter.getItem(position);
                                    if (merchantItem != null) {
                                        String itemCode = merchantItem.getItemCode();
                                        salesViewModel.UOM(itemCode,merchantId);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } else {
                            messageDialog.show();
                            messageDialog.loadingMessage(getString(R.string.no_items));
                            messageDialog.setListener(this);
                        }
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(itemsResponse.getStatus().getMessage());
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

    private void observeUOM() {
        salesViewModel.uomResponse.observe(this, this::consumeUOM);
    }

    private void consumeUOM(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                UOMResponse uomResponse = (UOMResponse) apiResponse.data;
                if (uomResponse != null) {
                    int statusCode = uomResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        List<UOM> uomList = uomResponse.getData();
                        if (uomList != null && uomList.size() > 0) {
                            uomAdapter = new UOMAdapter(
                                    this, R.layout.spinner_item, R.id.spinnerText, uomList);
                            spinnerUOM.setAdapter(uomAdapter);
                            spinnerUOM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    UOM uom = uomAdapter.getItem(position);
                                    if (uom != null) {
                                        etPrice.setText(Util.convertAmountWithSeparator(uom.getUnitPrice()));
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } else {
                            messageDialog.show();
                            messageDialog.loadingMessage(getString(R.string.no_items));
                            messageDialog.setListener(this);
                        }
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(uomResponse.getStatus().getMessage());
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

    @OnClick(R.id.btn_save)
    void saleItemSave() {
        String qty = etQty.getText().toString();
        String price = etPrice.getText().toString();
        String amount = etAmount.getText().toString();
        MerchantItem merchantItem = (MerchantItem) spinnerItemName.getSelectedItem();
        UOM uom = (UOM) spinnerUOM.getSelectedItem();

        if (!TextUtils.isEmpty(qty) && !TextUtils.isEmpty(price) &&
                !TextUtils.isEmpty(amount) && uom != null && merchantItem != null) {
            salesViewModel.preSale(merchantItem.getItemCode(), uom.getUomId(), merchantId, Integer.parseInt(qty));

        } else {
            Util.customToastMessage(SalesOrdersActivity.this, getString(R.string.save_alert), false);
        }
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
    public void onOkButtonClick() {

    }
}