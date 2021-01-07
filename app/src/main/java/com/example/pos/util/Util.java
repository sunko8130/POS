package com.example.pos.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pos.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Util {
    public static void customToastMessage(Context context, String message, boolean isLong) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_toast,null);
        TextView tvCustomToast = view.findViewById(R.id.tv_custom_toast);
        tvCustomToast.setText(message);
        // Toast...
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    public static String convertAmountWithSeparator(double amount) {
        NumberFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(amount);
    }
}
