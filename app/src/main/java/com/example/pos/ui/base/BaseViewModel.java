package com.example.pos.ui.base;


import com.example.pos.network.ApiResponse;

import java.io.IOException;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.HttpException;

import static com.example.pos.util.Constant.NO_INTERNET_CONNECTION;
import static com.example.pos.util.Constant.UNKNOWN_ERROR;


public class BaseViewModel extends ViewModel {
    protected final CompositeDisposable disposable = new CompositeDisposable();
    public final MutableLiveData<ApiResponse<?>> statesTownshipsResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> nrcFormatResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> registerResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> loginResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> generateReceiveNoResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> deliveryNumbersResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> deliveryOrdersResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> newReceiveSaveResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> receiveNumbersResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> receiveItemsResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> receiveItemViewResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> categoryResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> itemsResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> merchantItemsResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> itemsPriceResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> setSellingPriceResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> uomResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> generateSaleNoResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> preSaleResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> saleOrdersSaveResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> balanceResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> saleOrdersSearchResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> saleNumbersResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> saleOrderDetailResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> stockBalanceResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> searchItemResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> paymentItemsResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> deleteSaleItemResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> updateSaleItemResponse = new MutableLiveData<>();

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    protected String getErrorMessage(Throwable errorMessage) {
        if (errorMessage instanceof HttpException) {
            return UNKNOWN_ERROR;
        } else if (errorMessage instanceof IOException) {
            return NO_INTERNET_CONNECTION;
        } else {
            return UNKNOWN_ERROR;
        }
    }
}
