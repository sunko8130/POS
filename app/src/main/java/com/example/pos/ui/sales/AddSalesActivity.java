package com.example.pos.ui.sales;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pos.R;
import com.example.pos.common.ViewModelFactory;
import com.example.pos.delegate.OnRecyclerItemClickListener;
import com.example.pos.model.GenerateSaleNoResponse;
import com.example.pos.model.LoginData;
import com.example.pos.model.SaleOrdersSaveResponse;
import com.example.pos.model.SalesItem;
import com.example.pos.network.ApiResponse;
import com.example.pos.ui.base.BaseActivity;
import com.example.pos.util.Util;
import com.example.pos.widgets.MessageDialog;
import com.google.android.material.textfield.TextInputEditText;

import org.mmtextview.MMFontUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static com.example.pos.util.Constant.LOGIN_DATA;

public class AddSalesActivity extends BaseActivity implements OnRecyclerItemClickListener, MessageDialog.OnClickListener, AddNewSaleItemFragment.AddNewSaleItemListener {

    @BindView(R.id.rv_sales_item)
    RecyclerView rvSalesItem;

    @BindView(R.id.layout_sales)
    LinearLayout layoutSale;

    @BindView(R.id.et_sales_order_no)
    TextInputEditText etSaleNo;

    @BindView(R.id.tv_total_amount)
    TextView tvTotalAmount;

    @Inject
    ViewModelFactory viewModelFactory;
    private SalesViewModel salesViewModel;
    private SalesItemAdapter salesItemAdapter;
    private List<SalesItem> salesItemList;
    private LoginData loginData;
    private int merchantId;
    private double totalSaleAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            if (MMFontUtils.isSupportUnicode(this)) {
                getSupportActionBar().setTitle(getString(R.string.sales));
            } else {
                getSupportActionBar().setTitle(Html.fromHtml(MMFontUtils.uni2zg(getString(R.string.sales))));
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        salesViewModel = ViewModelProviders.of(this, viewModelFactory).get(SalesViewModel.class);
        salesItemList = new ArrayList<>();

        //init rv
        rvSalesItem.setLayoutManager(new LinearLayoutManager(this));
        rvSalesItem.setHasFixedSize(true);
        rvSalesItem.setItemAnimator(new DefaultItemAnimator());
//        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvSalesItem);
        salesItemAdapter = new SalesItemAdapter(this, this);
        rvSalesItem.setAdapter(salesItemAdapter);

        //login data
        loginData = Paper.book().read(LOGIN_DATA);
        if (loginData != null) {
            merchantId = loginData.getId();
        }

        //observe generate sale no
        observeGenerateSaleNo();
        salesViewModel.generateSaleNo();

        //observe sale orders save
        observeSaleOrdersSave();

    }

    private void observeSaleOrdersSave() {
        salesViewModel.saleOrdersSaveResponse.observe(this, this::consumeSaveOrdersSave);
    }

    private void consumeSaveOrdersSave(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                SaleOrdersSaveResponse saleOrdersSaveResponse = (SaleOrdersSaveResponse) apiResponse.data;
                if (saleOrdersSaveResponse != null) {
                    int statusCode = saleOrdersSaveResponse.getStatus().getCode();
                    messageDialog.show();
                    if (statusCode == 1) {
                        messageDialog.loadingMessage(getString(R.string.success_sales_orders_save));
                        messageDialog.setListener(this);
                    } else {
                        messageDialog.loadingMessage(saleOrdersSaveResponse.getStatus().getMessage());
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

    private void observeGenerateSaleNo() {
        salesViewModel.generateSaleNoResponse.observe(this, this::consumeGenerateSaleNo);
    }

    private void consumeGenerateSaleNo(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                GenerateSaleNoResponse generateSaleNoResponse = (GenerateSaleNoResponse) apiResponse.data;
                if (generateSaleNoResponse != null) {
                    int statusCode = generateSaleNoResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        //generate sale no
                        String generateReceiveNo = generateSaleNoResponse.getData();
                        etSaleNo.setText(generateReceiveNo);
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(generateSaleNoResponse.getStatus().getMessage());
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

    @OnClick(R.id.btn_add_sales_item)
    void addSalesItem() {
        AddNewSaleItemFragment addNewSaleItemFragment = new AddNewSaleItemFragment();
        addNewSaleItemFragment.show(getSupportFragmentManager(), "add new sale item");
    }

    @OnClick(R.id.btn_cancel)
    void cancel(){
        Intent intent = new Intent(AddSalesActivity.this, SalesActivity.class);
        startActivity(intent);
        finish();
    }

    private void addSalesItemView() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddSalesActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_sales_item, null);
        dialogBuilder.setView(dialogView);

        AppCompatSpinner spinnerItemName = dialogView.findViewById(R.id.spinner_item_name);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }


    @OnClick(R.id.btn_save)
    void saveSaleItems() {
        String saleNo = etSaleNo.getText().toString();
        if (TextUtils.isEmpty(saleNo)) {
            messageDialog.loadingMessage(getString(R.string.no_sale_number));
        } else {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("salesNo", saleNo);
            parameters.put("total", totalSaleAmount);
            parameters.put("merchantId", merchantId);
            parameters.put("saleList", salesItemList);
            salesViewModel.saleOrdersSave(parameters);
        }
        Log.e("sale list", salesItemList.size() + "");
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            SalesItem salesItem = salesItemAdapter.getItem(viewHolder.getAdapterPosition());
            salesItemList.remove(salesItem);
            salesItemAdapter.remove(salesItem);

            if (salesItemList.size() == 0) {
                tvTotalAmount.setVisibility(View.GONE);
            } else {
                tvTotalAmount.setVisibility(View.VISIBLE);
                totalAmount(salesItemList);
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(AddSalesActivity.this, R.color.md_red_600))
                    .addActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();
        }
    };

    @Override
    public void onItemClick(int position) {
        SalesItem salesItem = salesItemAdapter.getItem(position);
        salesItemList.remove(salesItem);
        salesItemAdapter.remove(salesItem);

        if (salesItemList.size() == 0) {
            tvTotalAmount.setVisibility(View.GONE);
        } else {
            tvTotalAmount.setVisibility(View.VISIBLE);
            totalAmount(salesItemList);
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

    @Override
    public void onOkButtonClick() {
        Intent intent = new Intent(AddSalesActivity.this, SalesActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void saveItem(SalesItem salesItem) {
        tvTotalAmount.setVisibility(View.VISIBLE);

        salesItemList.add(salesItem);
        salesItemAdapter.add(salesItem);

        totalAmount(salesItemList);

    }


    boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) rvSalesItem.getLayoutManager());
        int pos = layoutManager.findFirstVisibleItemPosition();
        int numItems = salesItemAdapter.getItemCount() - 1;
        return (pos >= numItems);
    }

    private void totalAmount(List<SalesItem> salesItemList) {
        //show total amount
        double totalAmount = 0;
        for (SalesItem salesItem : salesItemList) {
            totalAmount += salesItem.getAmount();
            Log.e("amount", salesItem.getAmount() + "");
        }
        Log.e("total amount", totalAmount + "");
        totalSaleAmount = totalAmount;
        Log.e("total sale amount", totalSaleAmount + "");
        String totalSaleAmount = Util.convertAmountWithSeparator(totalAmount);
        tvTotalAmount.setText(Html.fromHtml(mActivity.getResources().getString(R.string.total_order_price, totalSaleAmount)));
    }
}