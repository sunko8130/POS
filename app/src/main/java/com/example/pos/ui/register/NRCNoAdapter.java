package com.example.pos.ui.register;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.model.NRCNo;

import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.Nullable;

public class NRCNoAdapter extends ArrayAdapter<NRCNo> {
    private List<NRCNo> nrcNoList;

    @BindView(R.id.spinner_text)
    TextView spinnerText;

    public NRCNoAdapter(@NonNull Context context, int resource, int spinnerText, @NonNull List<NRCNo> nrcNoList) {
        super(context, resource, spinnerText, nrcNoList);
        this.nrcNoList = nrcNoList;
    }

    @Override
    public NRCNo getItem(int position) {
        return nrcNoList.get(position);
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
        NRCNo nrcNo = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = null;
        if (inflater != null) {
            v = inflater.inflate(R.layout.dropdown_menu_popup_item, null);
            ButterKnife.bind(this, v);
        }
        if (nrcNo != null) {
            spinnerText.setText(nrcNo.getCode() + "");
        }
        return v;
    }
}