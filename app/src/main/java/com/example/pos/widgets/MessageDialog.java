package com.example.pos.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pos.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageDialog extends Dialog implements View.OnClickListener {

    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.btn_ok)
    Button btnOk;
    private OnClickListener mListener;
    private boolean isClick;

    public MessageDialog(@NonNull Context context) {
        super(context);
    }

    public MessageDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MessageDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.message_dialog_layout);
        if (getWindow() != null) {
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        ButterKnife.bind(this);
        btnOk.setOnClickListener(this);
    }

    public void loadingMessage(String message) {
        tvMessage.setText(message);
    }

    @Override
    public void onClick(View view) {
        if (isClick) {
            mListener.onOkButtonClick();
        } else {
            dismiss();
        }
    }

    public void setListener(OnClickListener onClickListener) {
        isClick = true;
        mListener = onClickListener;
    }

    //disable back press
    @Override
    public void onBackPressed() {
    }

    public interface OnClickListener {
        void onOkButtonClick();
    }
}
