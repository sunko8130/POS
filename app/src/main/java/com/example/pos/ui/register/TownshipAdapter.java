package com.example.pos.ui.register;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.model.Township;

import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.Nullable;

public class TownshipAdapter extends ArrayAdapter<Township> {
    private List<Township> townshipList;
    @BindView(R.id.spinner_text)
    TextView spinnerText;

    public TownshipAdapter(@NonNull Context context, int resource, int spinnerText, @NonNull List<Township> townshipList) {
        super(context, resource, spinnerText, townshipList);
        this.townshipList = townshipList;
    }

    @Override
    public Township getItem(int position) {
        return townshipList.get(position);
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
        Township township = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = null;
        if (inflater != null) {
            v = inflater.inflate(R.layout.dropdown_menu_popup_item, null);
            ButterKnife.bind(this, v);
        }
        if (township != null) {
            spinnerText.setText(township.getName());
        }
        return v;

    }
}