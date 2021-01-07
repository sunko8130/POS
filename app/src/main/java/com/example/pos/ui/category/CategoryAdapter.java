package com.example.pos.ui.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.model.Category;

import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.Nullable;

public class CategoryAdapter extends ArrayAdapter<Category> {
    private List<Category> categoryList;

    @BindView(R.id.spinner_text)
    TextView spinnerText;

    public CategoryAdapter(@NonNull Context context, int resource, int spinnerText, @NonNull List<Category> categoryList) {
        super(context, resource, spinnerText, categoryList);
        this.categoryList = categoryList;
    }

    @Override
    public Category getItem(int position) {
        return categoryList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position);
    }

    private View initView(int position) {
        Category category = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = null;
        if (inflater != null) {
            v = inflater.inflate(R.layout.dropdown_menu_popup_item, null);
            ButterKnife.bind(this, v);
        }
        if (category != null) {
            spinnerText.setText(category.getName());
        }
        return v;
    }
}