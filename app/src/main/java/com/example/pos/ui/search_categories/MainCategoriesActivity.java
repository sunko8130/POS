package com.example.pos.ui.search_categories;

import android.os.Bundle;

import com.example.pos.R;
import com.example.pos.ui.base.BaseActivity;

import org.mmtextview.components.MMTextView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainCategoriesActivity extends BaseActivity {
    @BindView(R.id.spinner_main_category_name)
    AppCompatSpinner spinnerMainCategoryName;

    @BindView(R.id.rv_main_category)
    RecyclerView rvMainCategory;

    @BindView(R.id.tv_total_main_category_number)
    MMTextView tvTotalMainCategoryNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_categories);
        ButterKnife.bind(this);

        //recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvMainCategory.setLayoutManager(layoutManager);
        rvMainCategory.setHasFixedSize(true);
        rvMainCategory.setItemAnimator(new DefaultItemAnimator());
    }
}