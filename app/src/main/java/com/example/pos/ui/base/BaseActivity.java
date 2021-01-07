package com.example.pos.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.pos.R;
import com.example.pos.widgets.LoadingDialog;
import com.example.pos.widgets.MessageDialog;

import androidx.annotation.Nullable;
import dagger.android.support.DaggerAppCompatActivity;

public class BaseActivity extends DaggerAppCompatActivity {
    protected LoadingDialog loadingDialog;
    protected MessageDialog messageDialog;
    protected Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init loading
        loadingDialog = new LoadingDialog(this, R.style.CustomProgressBarTheme);
        messageDialog = new MessageDialog(this);
        mActivity = this;
    }

    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(mActivity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
