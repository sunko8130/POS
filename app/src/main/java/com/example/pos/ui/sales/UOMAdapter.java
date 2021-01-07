package com.example.pos.ui.sales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.model.UOM;

import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.Nullable;

public class UOMAdapter extends ArrayAdapter<UOM> {
    private List<UOM> uomList;

    @BindView(R.id.spinner_text)
    TextView spinnerText;

    public UOMAdapter(@NonNull Context context, int resource, int spinnerText, @NonNull List<UOM> uomList) {
        super(context, resource, spinnerText, uomList);
        this.uomList = uomList;
    }

    @Override
    public UOM getItem(int position) {
        return uomList.get(position);
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
        UOM uom = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = null;
        if (inflater != null) {
            v = inflater.inflate(R.layout.dropdown_menu_popup_item, null);
            ButterKnife.bind(this, v);
        }
        if (uom != null) {
            spinnerText.setText(uom.getUomName());
        }
        return v;
    }
}