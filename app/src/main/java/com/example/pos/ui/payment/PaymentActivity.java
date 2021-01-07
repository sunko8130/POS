package com.example.pos.ui.payment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.pos.R;
import com.example.pos.common.ViewModelFactory;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.DeliverNumbersResponse;
import com.example.pos.model.LoginData;
import com.example.pos.model.Payment;
import com.example.pos.model.PaymentResponse;
import com.example.pos.network.ApiResponse;
import com.example.pos.ui.base.BaseActivity;
import com.example.pos.ui.home.MainActivity;
import com.example.pos.widgets.MessageDialog;

import org.mmtextview.MMFontUtils;
import org.mmtextview.components.MMTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

import static com.example.pos.util.Constant.LOGIN_DATA;

public class PaymentActivity extends BaseActivity implements MessageDialog.OnClickListener, OnRecyclerItemClickListener {
    @BindView(R.id.btn_from_date)
    Button btnFromDate;

    @BindView(R.id.btn_to_date)
    Button btnToDate;

    @BindView(R.id.rv_payment)
    RecyclerView rvPayment;

    @BindView(R.id.tv_no_items)
    MMTextView tvNoItems;

    @BindView(R.id.tv_date)
    MMTextView tvDate;

    @BindView(R.id.spinner_delivery_number)
    AppCompatSpinner spinnerDeliveryNumber;

    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener fromDateListener;
    private DatePickerDialog.OnDateSetListener toDateListener;
    private String fromDate = "";
    private String toDate = "";
    private Date startDate = null, endDate = null;
    private LoginData loginData;
    private int merchantId;
    private PaymentViewModel paymentViewModel;

    @Inject
    ViewModelFactory viewModelFactory;
    private String deliveryNumber = "All";
    private PaymentAdapter paymentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            if (MMFontUtils.isSupportUnicode(this)) {
                getSupportActionBar().setTitle(getString(R.string.payment));
            } else {
                getSupportActionBar().setTitle(Html.fromHtml(MMFontUtils.uni2zg(getString(R.string.payment))));
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        paymentViewModel = ViewModelProviders.of(this, viewModelFactory).get(PaymentViewModel.class);

        //recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvPayment.setLayoutManager(layoutManager);
        rvPayment.setHasFixedSize(true);
        rvPayment.setNestedScrollingEnabled(false);
        rvPayment.setItemAnimator(new DefaultItemAnimator());
        paymentAdapter = new PaymentAdapter(this, this);

        //choose date
        chooseDate();

        //login data
        loginData = Paper.book().read(LOGIN_DATA);
        if (loginData != null) {
            merchantId = loginData.getId();
            paymentViewModel.deliveredNumbers(merchantId);
        }
        //observe deliver numbers
        observeDeliverNumbers();

        paymentItems();

        //observe payment items
        observePaymentItems();

    }

    private void observePaymentItems() {
        paymentViewModel.paymentItemsResponse.observe(this, this::consumePaymentItems);
    }

    private void consumePaymentItems(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                PaymentResponse paymentResponse = (PaymentResponse) apiResponse.data;
                if (paymentResponse != null) {
                    int statusCode = paymentResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        if (TextUtils.isEmpty(fromDate) && TextUtils.isEmpty(toDate)) {
                            tvDate.setVisibility(View.GONE);
                        } else {
                            tvDate.setVisibility(View.VISIBLE);
                            tvDate.setText(Html.fromHtml(getString(R.string.search_date, fromDate, toDate)));
                        }
                        //clear date
                        btnFromDate.setText(getString(R.string.receive_date_from));
                        btnToDate.setText(getString(R.string.receive_date_to));
                        fromDate = "";
                        toDate = "";

                        List<Payment> paymentItems = paymentResponse.getData();
                        if (paymentItems != null && paymentItems.size() > 0) {
                            rvPayment.setAdapter(paymentAdapter);
                            paymentAdapter.setItems(paymentItems);
                            tvNoItems.setVisibility(View.GONE);
                        } else {
                            tvNoItems.setVisibility(View.VISIBLE);
                        }
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(paymentResponse.getStatus().getMessage());
                    }
                }
                break;
            case ERROR:
                loadingDialog.dismiss();
                if (apiResponse.message != null) {
                    messageDialog.show();
                    messageDialog.loadingMessage(apiResponse.message);
                }
                break;
            default:
                break;
        }
    }

    private void observeDeliverNumbers() {
        paymentViewModel.deliveryNumbersResponse.observe(this, this::consumeDeliverNumbers);
    }

    private void consumeDeliverNumbers(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                DeliverNumbersResponse deliverNumbersResponse = (DeliverNumbersResponse) apiResponse.data;
                if (deliverNumbersResponse != null) {
                    int statusCode = deliverNumbersResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        List<String> deliverNumbersList = deliverNumbersResponse.getData();
                        if (deliverNumbersList != null && deliverNumbersList.size() > 0) {
                            ArrayAdapter<String> deliverNumbersAdapter = new ArrayAdapter<>(
                                    this, R.layout.spinner_item, R.id.spinnerText, deliverNumbersList);
                            spinnerDeliveryNumber.setAdapter(deliverNumbersAdapter);
                        } else {
                            messageDialog.show();
                            messageDialog.loadingMessage(getString(R.string.no_delivery_no_alert));
                            messageDialog.setListener(this);
                        }
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(deliverNumbersResponse.getStatus().getMessage());
                    }
                }
                break;
            case ERROR:
                loadingDialog.dismiss();
                if (apiResponse.message != null) {
                    messageDialog.show();
                    messageDialog.loadingMessage(apiResponse.message);
                }
                break;

            default:
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void chooseDate() {
        //from date
        btnFromDate.setOnClickListener(view -> {
            hideKeyboard();
            new DatePickerDialog(this, fromDateListener,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        //to date
        btnToDate.setOnClickListener(view -> {
            hideKeyboard();
            new DatePickerDialog(this, toDateListener,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        fromDateListener = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            fromDate();
        };

        toDateListener = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            toDate();
        };
    }

    private void fromDate() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        fromDate = sdf.format(calendar.getTime());
        startDate = calendar.getTime();
        btnFromDate.setText(sdf.format(calendar.getTime()));
    }

    private void toDate() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        toDate = sdf.format(calendar.getTime());
        endDate = calendar.getTime();
        btnToDate.setText(sdf.format(calendar.getTime()));
    }

    @OnClick(R.id.btn_search)
    void search() {
        tvDate.setVisibility(View.GONE);
        rvPayment.setAdapter(null);
        paymentAdapter.clear();

        deliveryNumber = spinnerDeliveryNumber.getSelectedItem().toString();

        //date compare
        if (startDate != null && endDate != null) {
            if (startDate.compareTo(endDate) > 0) {
                messageDialog.show();
                messageDialog.loadingMessage(getString(R.string.date_compare));
            } else {
                paymentItems();
            }
        } else {
            paymentItems();
        }

    }

    private void paymentItems() {
        paymentViewModel.payment(merchantId, deliveryNumber, fromDate, toDate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOkButtonClick() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemClick(int position) {

    }
}