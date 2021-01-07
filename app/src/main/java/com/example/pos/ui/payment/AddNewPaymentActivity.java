package com.example.pos.ui.payment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioGroup;

import com.example.pos.R;
import com.example.pos.ui.base.BaseActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.widget.AppCompatSpinner;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewPaymentActivity extends BaseActivity {

    @BindView(R.id.et_receive_no)
    TextInputEditText etCustomerName;

    @BindView(R.id.et_total_amount)
    TextInputEditText etTotalAmount;

    @BindView(R.id.et_total_paid_amount)
    TextInputEditText etTotalPaidAmount;

    @BindView(R.id.et_balance)
    TextInputEditText etBalance;

    @BindView(R.id.et_collect_by)
    TextInputEditText etCollectBy;

    @BindView(R.id.btn_receive_date)
    MaterialButton btnReceiveDate;

    @BindView(R.id.spinner_distributor)
    AppCompatSpinner spinnerDistributor;

    @BindView(R.id.first_group)
    RadioGroup firstGroup;

    @BindView(R.id.second_group)
    RadioGroup secondGroup;

    private boolean isChecking = true;
    private int mCheckedId = R.id.radio_myKyat;
    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener receiveDateListener;
    private String receiveDate = "";
    private Date orderReceiveDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_payment);
        ButterKnife.bind(this);
        //back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        firstGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1 && isChecking) {
                isChecking = false;
                secondGroup.clearCheck();
                mCheckedId = checkedId;
            }
            isChecking = true;
        });

        secondGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1 && isChecking) {
                isChecking = false;
                firstGroup.clearCheck();
                mCheckedId = checkedId;
            }
            isChecking = true;
        });

        //choose date
        chooseDate();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void chooseDate() {
        btnReceiveDate.setOnClickListener(view -> {
            hideKeyboard();
            new DatePickerDialog(this, receiveDateListener,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        receiveDateListener = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            receiveDate();
        };
    }

    private void receiveDate() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        receiveDate = sdf.format(calendar.getTime());
        orderReceiveDate = calendar.getTime();
        btnReceiveDate.setText(sdf.format(calendar.getTime()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}