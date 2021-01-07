package com.example.pos.ui.pricing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.common.ViewModelFactory;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.delegate.OnRecyclerMultiItemClickListener;
import com.example.pos.model.ItemPrice;
import com.example.pos.model.ItemsPriceResponse;
import com.example.pos.model.LoginData;
import com.example.pos.model.MerchantItem;
import com.example.pos.model.MerchantItemsResponse;
import com.example.pos.model.SalesItem;
import com.example.pos.model.SetSellingPriceResponse;
import com.example.pos.network.ApiResponse;
import com.example.pos.ui.base.BaseActivity;
import com.example.pos.ui.home.MainActivity;
import com.example.pos.util.Util;
import com.example.pos.widgets.ConfirmDialog;
import com.example.pos.widgets.MessageDialog;
import com.google.android.material.textfield.TextInputEditText;

import org.mmtextview.MMFontUtils;
import org.mmtextview.components.MMButton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static com.example.pos.util.Constant.LOGIN_DATA;

public class PricingActivity extends BaseActivity implements OnRecyclerItemClickListener, MessageDialog.OnClickListener, OnRecyclerMultiItemClickListener {

    @BindView(R.id.spinner_item_name)
    AppCompatSpinner spinnerItemName;

    @BindView(R.id.rv_pricing_item)
    RecyclerView rvPricingItems;

    @BindView(R.id.tv_no_items)
    TextView tvNoItems;

    @Inject
    ViewModelFactory viewModelFactory;
    private PricingViewModel pricingViewModel;
    private AddPriceItemAdapter addPriceItemAdapter;
    private List<SalesItem> addPriceList;
    private MerchantItemsAdapter itemsAdapter;
    private PricingAdapter pricingAdapter;
    private LoginData loginData;
    private int merchantId;
    private ConfirmDialog warningDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricing);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            if (MMFontUtils.isSupportUnicode(this)) {
                getSupportActionBar().setTitle(getString(R.string.selling_price));
            } else {
                getSupportActionBar().setTitle(Html.fromHtml(MMFontUtils.uni2zg(getString(R.string.selling_price))));
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        pricingViewModel = ViewModelProviders.of(this, viewModelFactory).get(PricingViewModel.class);
        warningDialog = new ConfirmDialog(PricingActivity.this);


        //recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvPricingItems.setLayoutManager(layoutManager);
        rvPricingItems.setHasFixedSize(true);
        rvPricingItems.setItemAnimator(new DefaultItemAnimator());
        rvPricingItems.setNestedScrollingEnabled(false);
        pricingAdapter = new PricingAdapter(this, this);

        //login data
        loginData = Paper.book().read(LOGIN_DATA);
        if (loginData != null) {
            merchantId = loginData.getId();
        }

        addPriceList = new ArrayList<>();
        //observe merchant items
        pricingViewModel.merchantItems(merchantId);
        observeItems();
        //observe pricing items
        observePricingItems();
        //observe set selling price
        observeSetSellingPrice();
    }

    private void observeSetSellingPrice() {
        pricingViewModel.setSellingPriceResponse.observe(this, this::consumeSetSellingPrice);
    }

    private void consumeSetSellingPrice(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                SetSellingPriceResponse setSellingPriceResponse = (SetSellingPriceResponse) apiResponse.data;
                if (setSellingPriceResponse != null) {
                    int statusCode = setSellingPriceResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        messageDialog.show();
                        messageDialog.loadingMessage(getString(R.string.success_set_selling_price));
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

    private void observePricingItems() {
        pricingViewModel.itemsPriceResponse.observe(this, this::consumeItemsPrice);
    }

    private void consumeItemsPrice(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                ItemsPriceResponse itemsPriceResponse = (ItemsPriceResponse) apiResponse.data;
                if (itemsPriceResponse != null) {
                    int statusCode = itemsPriceResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        List<ItemPrice> itemPrices = itemsPriceResponse.getData();
                        if (itemPrices != null && itemPrices.size() > 0) {
                            rvPricingItems.setAdapter(pricingAdapter);
                            pricingAdapter.setItems(itemPrices);
                            tvNoItems.setVisibility(View.GONE);
                        } else {
                            tvNoItems.setVisibility(View.VISIBLE);
                        }
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(itemsPriceResponse.getStatus().getMessage());
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
                            //item search
                            MerchantItem merchantItem = (MerchantItem) spinnerItemName.getSelectedItem();
                            if (merchantItem != null) {
                                String itemCode = merchantItem.getItemCode();
                                pricingViewModel.itemsPriceSearch(itemCode, merchantId);
                            }
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

    @OnClick(R.id.btn_search)
    void btnSearch() {
        rvPricingItems.setAdapter(null);
        pricingAdapter.clear();
        MerchantItem merchantItem = (MerchantItem) spinnerItemName.getSelectedItem();
        if (merchantItem != null) {
            String itemCode = merchantItem.getItemCode();
            pricingViewModel.itemsPriceSearch(itemCode, merchantId);
        }
    }

    private void addNewPrice() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_add_new_price, null);
        dialogBuilder.setView(dialogView);

        AppCompatSpinner spinnerItemName = dialogView.findViewById(R.id.spinner_item_name);
        AppCompatSpinner spinnerUOM = dialogView.findViewById(R.id.spinner_uom);
        TextInputEditText etPrice = dialogView.findViewById(R.id.et_price);
        MMButton btnSave = dialogView.findViewById(R.id.btn_save);
        MMButton btnCancel = dialogView.findViewById(R.id.btn_cancel);
        MMButton btnAddPrice = dialogView.findViewById(R.id.btn_add_price);
        RecyclerView rvSetPriceItems = dialogView.findViewById(R.id.rv_set_price_items);

        //init rv
        rvSetPriceItems.setLayoutManager(new LinearLayoutManager(mActivity));
        rvSetPriceItems.setNestedScrollingEnabled(false);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvSetPriceItems);
        addPriceItemAdapter = new AddPriceItemAdapter(mActivity, this);
        rvSetPriceItems.setAdapter(addPriceItemAdapter);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnAddPrice.setOnClickListener(v -> {
            //hide keyboard
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(dialogView.getWindowToken(), 0);

            String price = etPrice.getText().toString();
            if (!TextUtils.isEmpty(price)) {
//                SalesItem salesItem = new SalesItem(price, "price", "");
//                addPriceList.add(salesItem);
//                addPriceItemAdapter.add(salesItem);
            } else {
                Util.customToastMessage(PricingActivity.this, getString(R.string.save_alert), false);
            }
        });

        btnCancel.setOnClickListener(v -> alertDialog.dismiss());
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            SalesItem salesItem = addPriceItemAdapter.getItem(viewHolder.getAdapterPosition());
            addPriceList.remove(salesItem);
            addPriceItemAdapter.remove(salesItem);
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(PricingActivity.this, R.color.md_red_600))
                    .addActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();
        }
    };

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
    public void onClickAction(int pos, int value) {
        ItemPrice itemPrice = pricingAdapter.getItem(pos);
        warningDialog.show();
        warningDialog.loadingMessage(getString(R.string.set_selling_price_warning_msg));
        warningDialog.setListener(() -> {
            pricingViewModel.setSellingPrice(itemPrice.getId(), value);
            warningDialog.dismiss();
        });
    }

    @Override
    public void onOkButtonClick() {
        Intent intent = new Intent(PricingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}