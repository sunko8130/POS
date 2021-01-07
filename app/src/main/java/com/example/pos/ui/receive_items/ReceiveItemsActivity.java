package com.example.pos.ui.receive_items;

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
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.common.ViewModelFactory;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.LoginData;
import com.example.pos.model.OrderItem;
import com.example.pos.model.ReceiveItem;
import com.example.pos.model.ReceiveItemView;
import com.example.pos.model.ReceiveItemViewResponse;
import com.example.pos.model.ReceiveItemsResponse;
import com.example.pos.model.ReceiveNumbersResponse;
import com.example.pos.network.ApiResponse;
import com.example.pos.ui.base.BaseActivity;
import com.example.pos.util.Util;
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

public class ReceiveItemsActivity extends BaseActivity implements OnRecyclerItemClickListener {

    @BindView(R.id.btn_receive_from_date)
    Button btnFromDate;

    @BindView(R.id.btn_receive_to_date)
    Button btnToDate;

    @BindView(R.id.tv_date)
    MMTextView tvDate;

    @BindView(R.id.tv_total_amount)
    MMTextView tvTotalAmount;

    @BindView(R.id.tv_no_receive_items)
    MMTextView tvNoReceiveItems;

    @BindView(R.id.spinner_receive_number)
    AppCompatSpinner spinnerReceiveNumber;

    @BindView(R.id.rv_receive_items)
    RecyclerView rvReceiveItems;

    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener fromDateListener;
    private DatePickerDialog.OnDateSetListener toDateListener;
    private String fromDate = "";
    private String toDate = "";
    private Date startDate = null, endDate = null;
    private ReceiveItemsAdapter receiveItemsAdapter;
    private String receiveNumber = "All";
    private MessageDialog loading;

    @Inject
    ViewModelFactory viewModelFactory;

    private ReceiveItemsViewModel receiveItemsViewModel;
    private LoginData loginData;
    private int merchantId;

    private AddNewReceiveItemsAdapter addNewReceiveItemsAdapter;
    private TextView tvReceiveNo, tvReceiverName, tvDeliveryNo;
    private RecyclerView rvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_items);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            if (MMFontUtils.isSupportUnicode(this)) {
                getSupportActionBar().setTitle(getString(R.string.receive_items));
            } else {
                getSupportActionBar().setTitle(Html.fromHtml(MMFontUtils.uni2zg(getString(R.string.receive_items))));
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        receiveItemsViewModel = ViewModelProviders.of(this, viewModelFactory).get(ReceiveItemsViewModel.class);

        //recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvReceiveItems.setLayoutManager(layoutManager);
        rvReceiveItems.setHasFixedSize(true);
        rvReceiveItems.setItemAnimator(new DefaultItemAnimator());
        rvReceiveItems.setNestedScrollingEnabled(false);
        receiveItemsAdapter = new ReceiveItemsAdapter(this, this);

        //login data
        loginData = Paper.book().read(LOGIN_DATA);
        if (loginData != null) {
            merchantId = loginData.getId();
        }

        //choose date
        chooseDate();

        //observe receive numbers
        observeReceiveNumbers();
        receiveItemsViewModel.receiveNumbers(merchantId);

        retrieveReceiveItems();

        ///observe receive items
        observeReceiveItems();

    }

    @OnClick(R.id.btn_add_receive_item)
    void addReceiveItem() {
        Intent intent = new Intent(this, AddNewReceiveItemActivity.class);
        startActivity(intent);
    }

    private void observeReceiveItems() {
        receiveItemsViewModel.receiveItemsResponse.observe(this, this::consumeReceiveItems);
    }

    private void consumeReceiveItems(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                ReceiveItemsResponse receiveItemsResponse = (ReceiveItemsResponse) apiResponse.data;
                if (receiveItemsResponse != null) {
                    int statusCode = receiveItemsResponse.getStatus().getCode();
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

                        List<ReceiveItem> receiveItems = receiveItemsResponse.getData();
                        if (receiveItems != null && receiveItems.size() > 0) {
                            //show total amount
                            double totalAmount = 0;
                            for (ReceiveItem receiveItem : receiveItems) {
                                totalAmount += receiveItem.getTotalAmount();
                            }
                            String totalPurchaseAmount = Util.convertAmountWithSeparator(totalAmount);
                            tvTotalAmount.setText(Html.fromHtml(mActivity.getResources().getString(R.string.total_order_price, totalPurchaseAmount)));

                            rvReceiveItems.setAdapter(receiveItemsAdapter);
                            receiveItemsAdapter.setItems(receiveItems);
                            tvNoReceiveItems.setVisibility(View.GONE);
                            tvTotalAmount.setVisibility(View.VISIBLE);
                        } else {
                            tvTotalAmount.setVisibility(View.GONE);
                            tvNoReceiveItems.setVisibility(View.VISIBLE);
                        }
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(receiveItemsResponse.getStatus().getMessage());
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

    private void observeReceiveNumbers() {
        receiveItemsViewModel.receiveNumbersResponse.observe(this, this::consumeReceiveNumbers);
    }

    private void consumeReceiveNumbers(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                ReceiveNumbersResponse receiveNumbersResponse = (ReceiveNumbersResponse) apiResponse.data;
                if (receiveNumbersResponse != null) {
                    int statusCode = receiveNumbersResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        List<String> receiveNumbersList = receiveNumbersResponse.getData();
                        if (receiveNumbersList != null && receiveNumbersList.size() > 0) {
                            ArrayAdapter<String> deliverNumbersAdapter = new ArrayAdapter<>(
                                    this, R.layout.spinner_item, R.id.spinnerText, receiveNumbersList);
                            spinnerReceiveNumber.setAdapter(deliverNumbersAdapter);
                        } else {
                            messageDialog.show();
                            messageDialog.loadingMessage(getString(R.string.no_receive_numbers_alert));
                        }
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(receiveNumbersResponse.getStatus().getMessage());
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
        tvTotalAmount.setVisibility(View.GONE);
        tvDate.setVisibility(View.GONE);
        rvReceiveItems.setAdapter(null);
        receiveItemsAdapter.clear();

        receiveNumber = spinnerReceiveNumber.getSelectedItem().toString();

        //date compare
        if (startDate != null && endDate != null) {
            if (startDate.compareTo(endDate) > 0) {
                messageDialog.show();
                messageDialog.loadingMessage(getString(R.string.date_compare));
            } else {
                retrieveReceiveItems();
            }
        } else {
            retrieveReceiveItems();
        }

    }

    private void retrieveReceiveItems() {
        receiveItemsViewModel.receiveItems(receiveNumber, fromDate, toDate, merchantId);
    }

    @Override
    public void onItemClick(int position) {
        receiveDetailView(position);
    }

    private void receiveDetailView(int position) {
        ReceiveItem receiveItem = receiveItemsAdapter.getItem(position);
        ReceiveItemDetailFragment receiveItemDetailFragment = ReceiveItemDetailFragment.newInstance(receiveItem);
        receiveItemDetailFragment.setCancelable(false);
        receiveItemDetailFragment.show(getSupportFragmentManager(), "receive detail");


//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
//        LayoutInflater inflater = mActivity.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.receive_detail_layout, null);
//        dialogBuilder.setView(dialogView);
//
//        tvReceiveNo = dialogView.findViewById(R.id.tv_receive_no);
//        tvReceiverName = dialogView.findViewById(R.id.tv_receiver_name);
//        tvDeliveryNo = dialogView.findViewById(R.id.tv_delivery_order_no);
//        rvItems = dialogView.findViewById(R.id.rv_new_receive_items);
//        ImageButton btnDelete = dialogView.findViewById(R.id.btn_delete);
//
//        receiveItemsViewModel.receiveItemView(receiveItem.getReceiveOrderNo(), receiveItem.getReceivedPersonName());
//        observeReceiveItemView();
//
//        //init rv
//        rvItems.setLayoutManager(new LinearLayoutManager(mActivity));
//        rvItems.setNestedScrollingEnabled(false);
//        addNewReceiveItemsAdapter = new AddNewReceiveItemsAdapter(mActivity, this);
//        rvItems.setAdapter(addNewReceiveItemsAdapter);
//
//        AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.show();
//
//        btnDelete.setOnClickListener(v -> {
//            alertDialog.dismiss();
//        });
    }

    private void observeReceiveItemView() {
        receiveItemsViewModel.receiveItemViewResponse.observe(this, this::consumeReceiveItemView);
    }

    private void consumeReceiveItemView(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                ReceiveItemViewResponse receiveItemViewResponse = (ReceiveItemViewResponse) apiResponse.data;
                if (receiveItemViewResponse != null) {
                    int statusCode = receiveItemViewResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        ReceiveItemView receiveItemView = receiveItemViewResponse.getData();
                        if (receiveItemView != null) {
                            tvReceiveNo.setText(receiveItemView.getReceiveOrderNo());
                            tvReceiverName.setText(receiveItemView.getReceivePersonName());
                            List<OrderItem> orderItems = receiveItemView.getResList();
                            if (orderItems != null && orderItems.size() > 0) {
                                rvItems.setAdapter(addNewReceiveItemsAdapter);
                                addNewReceiveItemsAdapter.setItems(orderItems);
                            }
                        }
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(receiveItemViewResponse.getStatus().getMessage());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}