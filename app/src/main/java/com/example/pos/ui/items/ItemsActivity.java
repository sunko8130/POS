package com.example.pos.ui.items;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.common.ViewModelFactory;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.Bal;
import com.example.pos.model.Category;
import com.example.pos.model.CategoryResponse;
import com.example.pos.model.ItemSearchResponse;
import com.example.pos.model.LoginData;
import com.example.pos.model.MerchantItem;
import com.example.pos.model.MerchantItemsResponse;
import com.example.pos.model.SearchItem;
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

public class ItemsActivity extends BaseActivity implements MessageDialog.OnClickListener, OnRecyclerItemClickListener {
    @BindView(R.id.spinner_item_name)
    AppCompatSpinner spinnerItemName;

    @BindView(R.id.spinner_main_category_name)
    AppCompatSpinner spinnerMainCategoryName;

    @BindView(R.id.rv_items)
    RecyclerView rvItems;

    @BindView(R.id.tv_no_items)
    TextView tvNoItems;

    @BindView(R.id.tv_number_of_main_item)
    TextView tvNumberOfMainItem;

    @BindView(R.id.tv_number_of_sub_item)
    TextView tvNumberOfSubItem;

    @BindView(R.id.tv_number_of_item)
    TextView tvNumberOfItem;

    @Inject
    ViewModelFactory viewModelFactory;
    private SearchItemViewModel searchItemViewModel;
    private LoginData loginData;
    private int merchantId;
    private int categoryId = 0;
    private int merchantItemId = 0;
    private CategoryAdapter categoryAdapter;
    private MerchantItemsAdapter itemsAdapter;
    private ItemsAdapter searchItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            if (MMFontUtils.isSupportUnicode(this)) {
                getSupportActionBar().setTitle(getString(R.string.category_search_title));
            } else {
                getSupportActionBar().setTitle(Html.fromHtml(MMFontUtils.uni2zg(getString(R.string.category_search_title))));
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        searchItemViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchItemViewModel.class);

        //recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(layoutManager);
        rvItems.setHasFixedSize(true);
        rvItems.setNestedScrollingEnabled(false);
        rvItems.setItemAnimator(new DefaultItemAnimator());
        searchItemsAdapter = new ItemsAdapter(this, this);

        //login data
        loginData = Paper.book().read(LOGIN_DATA);
        if (loginData != null) {
            merchantId = loginData.getId();
        }

        searchItemViewModel.allCategory();
        //observe category
        observeCategory();
        //observe items
        observeItems();
        //observe search items
        observeSearchItems();

        searchItems();
    }

    private void observeSearchItems() {
        searchItemViewModel.searchItemResponse.observe(this, this::consumeSearchItems);
    }

    private void consumeSearchItems(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                ItemSearchResponse itemSearchResponse = (ItemSearchResponse) apiResponse.data;
                if (itemSearchResponse != null) {
                    int statusCode = itemSearchResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        List<SearchItem> searchItems = itemSearchResponse.getData();
                        Bal bal = itemSearchResponse.getBal();
                        if (bal != null) {
                            if (bal.getCateCount() != 0) {
                                tvNumberOfMainItem.setText(Html.fromHtml(getResources().getString(R.string.number_of_main_category, String.valueOf(bal.getCateCount()))));
                                tvNumberOfMainItem.setVisibility(View.VISIBLE);

                            }
                            if (bal.getSubCateCount() != 0) {
                                tvNumberOfSubItem.setText(Html.fromHtml(getResources().getString(R.string.number_of_sub_category, String.valueOf(bal.getSubCateCount()))));
                                tvNumberOfSubItem.setVisibility(View.VISIBLE);

                            }
                            if (bal.getItemCount() != 0) {
                                tvNumberOfItem.setText(Html.fromHtml(getResources().getString(R.string.number_of_item, String.valueOf(bal.getItemCount()))));
                                tvNumberOfItem.setVisibility(View.VISIBLE);

                            }
                        }
                        if (searchItems != null && searchItems.size() > 0) {
                            rvItems.setAdapter(searchItemsAdapter);
                            searchItemsAdapter.setItems(searchItems);
                            tvNoItems.setVisibility(View.GONE);
                        } else {
                            tvNoItems.setVisibility(View.VISIBLE);
                        }
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(itemSearchResponse.getStatus().getMessage());
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
        searchItemViewModel.merchantItemsResponse.observe(this, this::consumeItems);
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
        searchItemViewModel.categoryResponse.observe(this, this::consumeCategory);
    }

    private void consumeCategory(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                searchItemViewModel.allMerchantItems(merchantId);
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
        rvItems.setAdapter(null);
        searchItemsAdapter.clear();
        tvNumberOfMainItem.setVisibility(View.GONE);
        tvNumberOfSubItem.setVisibility(View.GONE);
        tvNumberOfItem.setVisibility(View.GONE);
        MerchantItem merchantItem = (MerchantItem) spinnerItemName.getSelectedItem();
        Category category = (Category) spinnerMainCategoryName.getSelectedItem();
        categoryId = category.getId();
        merchantItemId = merchantItem.getItemId();
        searchItems();

    }

    private void searchItems() {
        searchItemViewModel.searchItems(categoryId, merchantItemId, merchantId);
    }

    @Override
    public void onOkButtonClick() {
        Intent intent = new Intent(ItemsActivity.this, MainActivity.class);
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
}