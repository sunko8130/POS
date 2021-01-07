package com.example.pos.ui.register;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.model.NRCNation;

import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.Nullable;

public class NRCNationAdapter extends ArrayAdapter<NRCNation> {
    private List<NRCNation> nrcNationNoList;

    @BindView(R.id.spinner_text)
    TextView spinnerText;

    public NRCNationAdapter(@NonNull Context context, int resource, int spinnerText, @NonNull List<NRCNation> nrcNationNoList) {
        super(context, resource, spinnerText, nrcNationNoList);
        this.nrcNationNoList = nrcNationNoList;
    }

    @Override
    public NRCNation getItem(int position) {
        return nrcNationNoList.get(position);
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
        NRCNation nrcNation = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = null;
        if (inflater != null) {
            v = inflater.inflate(R.layout.dropdown_menu_popup_item, null);
            ButterKnife.bind(this, v);
        }
        if (nrcNation != null) {
            spinnerText.setText(nrcNation.getNrc());
        }
        return v;
    }
}