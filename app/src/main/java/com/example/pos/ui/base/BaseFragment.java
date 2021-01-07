package com.example.pos.ui.base;

import android.content.Context;
import android.os.Bundle;

import com.example.pos.R;
import com.example.pos.widgets.LoadingDialog;
import com.example.pos.widgets.MessageDialog;

import androidx.annotation.Nullable;
import dagger.android.support.DaggerFragment;

public class BaseFragment extends DaggerFragment {
    protected LoadingDialog loadingDialog;
    protected MessageDialog messageDialog;
    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //init loading
        loadingDialog = new LoadingDialog(mContext, R.style.CustomProgressBarTheme);
        messageDialog = new MessageDialog(mContext);

    }

}
