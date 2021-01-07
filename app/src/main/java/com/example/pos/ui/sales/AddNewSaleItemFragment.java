package com.example.pos.ui.sales;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.example.pos.R;
import com.example.pos.common.ViewModelFactory;
import com.example.pos.model.LoginData;
import com.example.pos.model.MerchantItem;
import com.example.pos.model.MerchantItemsResponse;
import com.example.pos.model.PreSaleResponse;
import com.example.pos.model.SalesItem;
import com.example.pos.model.UOM;
import com.example.pos.model.UOMResponse;
import com.example.pos.network.ApiResponse;
import com.example.pos.ui.base.BaseDialogFragment;
import com.example.pos.ui.pricing.MerchantItemsAdapter;
import com.example.pos.ui.pricing.PricingViewModel;
import com.example.pos.util.Util;
import com.example.pos.widgets.MMEditText;
import com.example.pos.widgets.MessageDialog;
import com.google.android.material.textfield.TextInputEditText;

import org.mmtextview.MMFontUtils;

import java.util.List;

import javax.inject.Inject;

import static com.example.pos.util.Constant.LOGIN_DATA;

public class AddNewSaleItemFragment extends BaseDialogFragment implements MessageDialog.OnClickListener {

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

    private View rootView;
    private AddNewSaleItemListener listener;
    @Inject
    ViewModelFactory viewModelFactory;
    private SalesViewModel salesViewModel;
    private PricingViewModel pricingViewModel;
    private int merchantId;
    private MerchantItemsAdapter itemsAdapter;
    private UOMAdapter uomAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        initViews();
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setView(rootView)
                .setCancelable(false)
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        return alertDialog;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        salesViewModel = ViewModelProviders.of(this, viewModelFactory).get(SalesViewModel.class);
        pricingViewModel = ViewModelProviders.of(this, viewModelFactory).get(PricingViewModel.class);

        //login data
        LoginData loginData = Paper.book().read(LOGIN_DATA);
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

        etAmount.setOnTouchListener((v, event) -> {
            String qty = etQty.getText().toString();
            String price = etPrice.getText().toString().replace(",", "");
            if (TextUtils.isEmpty(qty)) {
                messageDialog.show();
                messageDialog.loadingMessage(getString(R.string.enter_qty));
            } else if (TextUtils.isEmpty(price)) {
                messageDialog.show();
                messageDialog.loadingMessage(getString(R.string.enter_price));
            } else {
                double total = Double.parseDouble(qty) * Double.parseDouble(price);
                etAmount.setText(Util.convertAmountWithSeparator(total));
            }
            return false;
        });
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
                        SalesItem salesItem = new SalesItem();
                        salesItem.setItemCode(merchantItem.getItemCode());
                        salesItem.setItemName(merchantItem.getItemName());
                        salesItem.setQuantity(qty);
                        salesItem.setPrice(Double.parseDouble(price.replace(",", "")));
                        salesItem.setAmount(Double.parseDouble(amount.replace(",", "")));
                        salesItem.setUom(uom.getUomName());
                        salesItem.setUomId(uom.getUomId());
                        listener.saveItem(salesItem);
                        dismiss();
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
                                    mContext, R.layout.spinner_item, R.id.spinnerText, items);
                            spinnerItemName.setAdapter(itemsAdapter);
                            spinnerItemName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    //item search
                                    MerchantItem merchantItem = itemsAdapter.getItem(position);
                                    if (merchantItem != null) {
                                        String itemCode = merchantItem.getItemCode();
                                        salesViewModel.UOM(itemCode, merchantId);
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
                                    mContext, R.layout.spinner_item, R.id.spinnerText, uomList);
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
                            messageDialog.loadingMessage(getString(R.string.no_uom));
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
            Util.customToastMessage(mContext, getString(R.string.save_alert), false);
        }
    }

    @OnClick(R.id.btn_cancel)
    void cancel() {
        dismiss();
    }

    private void initViews() {
        rootView = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_add_new_sale_item, null, false);
        ButterKnife.bind(this, rootView);
        if (MMFontUtils.isSupportUnicode(mContext)) {
            etQty.setHint(getResources().getString(R.string.quantity));
        } else {
            etQty.setHint(Html.fromHtml(MMFontUtils.uni2zg(getString(R.string.quantity))));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddNewSaleItemListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement AddNewSaleItemListener");
        }
    }

    @Override
    public void onOkButtonClick() {
        messageDialog.dismiss();
        dismiss();
    }

    public interface AddNewSaleItemListener {
        void saveItem(SalesItem salesItem);
    }
}