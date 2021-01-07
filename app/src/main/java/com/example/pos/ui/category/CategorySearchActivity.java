package com.example.pos.ui.category;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;

import com.example.pos.R;
import com.example.pos.common.ViewModelFactory;
import com.example.pos.model.Category;
import com.example.pos.model.CategoryResponse;
import com.example.pos.model.LoginData;
import com.example.pos.model.MerchantItem;
import com.example.pos.model.MerchantItemsResponse;
import com.example.pos.network.ApiResponse;
import com.example.pos.ui.base.BaseActivity;
import com.example.pos.ui.home.MainActivity;
import com.example.pos.ui.pricing.MerchantItemsAdapter;
import com.example.pos.widgets.MessageDialog;

import org.mmtextview.MMFontUtils;
import org.mmtextview.components.MMTextView;

import java.util.List;

import javax.inject.Inject;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

import static com.example.pos.util.Constant.LOGIN_DATA;

public class CategorySearchActivity extends BaseActivity implements MessageDialog.OnClickListener {

    @BindView(R.id.spinner_sub_category_name)
    AppCompatSpinner spinnerSubCategoryName;

    @BindView(R.id.spinner_main_category_name)
    AppCompatSpinner spinnerMainCategoryName;

    @BindView(R.id.spinner_item_name)
    AppCompatSpinner spinnerItemName;

    @BindView(R.id.rv_sub_category)
    RecyclerView rvCategory;

    @BindView(R.id.tv_total_sub_category_number)
    MMTextView tvTotalSubCategoryNumber;

    @Inject
    ViewModelFactory viewModelFactory;

    private CategoryViewModel categoryViewModel;
    private CategoryAdapter categoryAdapter;
    private MerchantItemsAdapter itemsAdapter;
    private LoginData loginData;
    private int merchantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            if (MMFontUtils.isSupportUnicode(this)) {
                getSupportActionBar().setTitle(getString(R.string.category_search_title));
            } else {
                getSupportActionBar().setTitle(Html.fromHtml(MMFontUtils.uni2zg(getString(R.string.category_search_title))));
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        categoryViewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoryViewModel.class);

        //recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvCategory.setLayoutManager(layoutManager);
        rvCategory.setHasFixedSize(true);
        rvCategory.setItemAnimator(new DefaultItemAnimator());

        //login data
        loginData = Paper.book().read(LOGIN_DATA);
        if (loginData != null) {
            merchantId = loginData.getId();
        }

        categoryViewModel.allCategory();
        //observe category
        observeCategory();
        //observe items
        observeItems();

    }

    private void observeItems() {
        categoryViewModel.merchantItemsResponse.observe(this, this::consumeItems);
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
        categoryViewModel.categoryResponse.observe(this, this::consumeCategory);
    }

    private void consumeCategory(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                categoryViewModel.allMerchantItems(merchantId);
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
        Intent intent = new Intent(CategorySearchActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}