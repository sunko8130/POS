package com.example.pos.ui.home;

import android.view.View;

import com.example.pos.R;

import org.mmtextview.components.MMTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToolbarViewHolder {

    @BindView(R.id.toolbar_title)
    MMTextView tvToolbarName;

    ToolbarViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}