package com.example.pos.ui.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.model.Item;

import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.Nullable;

public class ItemsAdapter extends ArrayAdapter<Item> {
    private List<Item> itemList;

    @BindView(R.id.spinner_text)
    TextView spinnerText;

    public ItemsAdapter(@NonNull Context context, int resource, int spinnerText, @NonNull List<Item> itemList) {
        super(context, resource, spinnerText, itemList);
        this.itemList = itemList;
    }

    @Override
    public Item getItem(int position) {
        return itemList.get(position);
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
        Item item = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = null;
        if (inflater != null) {
            v = inflater.inflate(R.layout.dropdown_menu_popup_item, null);
            ButterKnife.bind(this, v);
        }
        if (item != null) {
            spinnerText.setText(item.getName());
        }
        return v;
    }
}