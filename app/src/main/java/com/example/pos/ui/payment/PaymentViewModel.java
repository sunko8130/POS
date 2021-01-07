package com.example.pos.ui.payment;

import com.example.pos.model.DeliverNumbersResponse;
import com.example.pos.model.PaymentResponse;
import com.example.pos.network.ApiResponse;
import com.example.pos.network.NetworkRepository;
import com.example.pos.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class PaymentViewModel extends BaseViewModel {
    private NetworkRepository networkRepository;

    @Inject
    public PaymentViewModel(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

    public void payment(int merchantId, String orderNo, String startDate, String endDate) {
        disposable.add(networkRepository.payment(merchantId, orderNo, startDate, endDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> paymentItemsResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<PaymentResponse>() {
                    @Override
                    public void onSuccess(PaymentResponse value) {
                        paymentItemsResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        paymentItemsResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }

    public void deliveredNumbers(int id) {
        disposable.add(networkRepository.deliveredNumbers(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> deliveryNumbersResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<DeliverNumbersResponse>() {
                    @Override
                    public void onSuccess(DeliverNumbersResponse value) {
                        deliveryNumbersResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        deliveryNumbersResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }
}
