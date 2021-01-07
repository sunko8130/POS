package com.example.pos.ui.stock_balance;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.common.ViewModelFactory;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.Category;
import com.example.pos.model.CategoryResponse;
import com.example.pos.model.LoginData;
import com.example.pos.model.MerchantItem;
import com.example.pos.model.MerchantItemsResponse;
import com.example.pos.model.StockBalance;
import com.example.pos.model.StockBalanceResponse;
import com.example.pos.network.ApiResponse;
import com.example.pos.ui.base.BaseActivity;
import com.example.pos.ui.category.CategoryAdapter;
import com.example.pos.ui.home.MainActivity;
import com.example.pos.ui.pricing.MerchantItemsAdapter;
import com.example.pos.widgets.MessageDialog;

import org.mmtextview.MMFontUtils;

import java.util.List;

import javax.inject.Inject;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

import static com.example.pos.util.Constant.LOGIN_DATA;

public class StockBalanceActivity extends BaseActivity implements MessageDialog.OnClickListener, OnRecyclerItemClickListener {

    @BindView(R.id.spinner_main_category_name)
    AppCompatSpinner spinnerMainCategoryName;

    @BindView(R.id.spinner_item_name)
    AppCompatSpinner spinnerItemName;

    @BindView(R.id.rv_stock_balance)
    RecyclerView rvStockBalance;

    @BindView(R.id.tv_no_items)
    TextView tvNoItems;

    @Inject
    ViewModelFactory viewModelFactory;
    private CategoryAdapter categoryAdapter;
    private MerchantItemsAdapter itemsAdapter;
    private StockBalanceAdapter stockBalanceAdapter;
    private StockBalanceViewModel stockBalanceViewModel;
    private LoginData loginData;
    private int merchantId;
    private int categoryId = 0;
    private int merchantItemId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_balance);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            if (MMFontUtils.isSupportUnicode(this)) {
                getSupportActionBar().setTitle(getString(R.string.stock_balance));
            } else {
                getSupportActionBar().setTitle(Html.fromHtml(MMFontUtils.uni2zg(getString(R.string.stock_balance))));
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        stockBalanceViewModel = ViewModelProviders.of(this, viewModelFactory).get(StockBalanceViewModel.class);

        //recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvStockBalance.setLayoutManager(layoutManager);
        rvStockBalance.setHasFixedSize(true);
        rvStockBalance.setNestedScrollingEnabled(false);
        rvStockBalance.setItemAnimator(new DefaultItemAnimator());
        stockBalanceAdapter = new StockBalanceAdapter(this, this);

        //login data
        loginData = Paper.book().read(LOGIN_DATA);
        if (loginData != null) {
            merchantId = loginData.getId();
        }

        stockBalanceViewModel.allCategory();
        //observe category
        observeCategory();
        //observe items
        observeItems();
        //observe stock balance
        observeStockBalance();

        //stock balance
        stockBalance();

    }

    private void observeStockBalance() {
        stockBalanceViewModel.stockBalanceResponse.observe(this, this::consumeStockBalance);
    }

    private void consumeStockBalance(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                StockBalanceResponse stockBalanceResponse = (StockBalanceResponse) apiResponse.data;
                if (stockBalanceResponse != null) {
                    int statusCode = stockBalanceResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        List<StockBalance> stockBalances = stockBalanceResponse.getData();
                        if (stockBalances != null && stockBalances.size() > 0) {
                            rvStockBalance.setAdapter(stockBalanceAdapter);
                            stockBalanceAdapter.setItems(stockBalances);
                            tvNoItems.setVisibility(View.GONE);
                        } else {
                            tvNoItems.setVisibility(View.VISIBLE);
                        }
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(stockBalanceResponse.getStatus().getMessage());
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
        stockBalanceViewModel.merchantItemsResponse.observe(this, this::consumeItems);
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

    private void observeCategory() {
        stockBalanceViewModel.categoryResponse.observe(this, this::consumeCategory);
    }

    private void consumeCategory(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                stockBalanceViewModel.allMerchantItems(merchantId);
                CategoryResponse categoryResponse = (CategoryResponse) apiResponse.data;
                if (categoryResponse != null) {
                    int statusCode = categoryResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        List<Category> categories = categoryResponse.getData();
                        if (categories != null && categories.size() > 0) {
                            categoryAdapter = new CategoryAdapter(mActivity,
                                    R.layout.dropdown_menu_popup_item, R.id.spinner_text, categories);
                            spinnerMainCategoryName.setAdapter(categoryAdapter);
                        } else {
                            messageDialog.show();
                            messageDialog.loadingMessage(getString(R.string.no_items));
                            messageDialog.setListener(this);
                        }
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(categoryResponse.getStatus().getMessage());
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
        rvStockBalance.setAdapter(null);
        stockBalanceAdapter.clear();
        MerchantItem merchantItem = (MerchantItem) spinnerItemName.getSelectedItem();
        Category category = (Category) spinnerMainCategoryName.getSelectedItem();
        categoryId = category.getId();
        merchantItemId = merchantItem.getItemId();
        stockBalance();

    }

    private void stockBalance() {
        stockBalanceViewModel.stockBalance(categoryId, merchantItemId, merchantId);
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
        Intent intent = new Intent(StockBalanceActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemClick(int position) {

    }
}