package com.example.pos.ui.receive_items;

import com.example.pos.model.DeliverNumbersResponse;
import com.example.pos.model.DeliveryOrdersResponse;
import com.example.pos.model.GenerateReceiveNoResponse;
import com.example.pos.model.NewReceiveSaveResponse;
import com.example.pos.model.ReceiveItemViewResponse;
import com.example.pos.model.ReceiveItemsResponse;
import com.example.pos.model.ReceiveNumbersResponse;
import com.example.pos.network.ApiResponse;
import com.example.pos.network.NetworkRepository;
import com.example.pos.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ReceiveItemsViewModel extends BaseViewModel {
    private NetworkRepository networkRepository;

    @Inject
    public ReceiveItemsViewModel(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

    public void generateReceiveNo() {
        disposable.add(networkRepository.generateReceiveNo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> generateReceiveNoResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<GenerateReceiveNoResponse>() {
                    @Override
                    public void onSuccess(GenerateReceiveNoResponse value) {
                        generateReceiveNoResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        generateReceiveNoResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }

    public void deliverNumbers(int id) {
        disposable.add(networkRepository.deliverNumbers(id)
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

    public void deliveryOrders(String orderNo) {
        disposable.add(networkRepository.deliveryOrders(orderNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> deliveryOrdersResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<DeliveryOrdersResponse>() {
                    @Override
                    public void onSuccess(DeliveryOrdersResponse value) {
                        deliveryOrdersResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        deliveryOrdersResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }

    public void newReceiveSave(String receiveNo, String receiveBy, String orderNo, int merchantId) {
        disposable.add(networkRepository.newReceiveSave(receiveNo, receiveBy, orderNo, merchantId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> newReceiveSaveResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<NewReceiveSaveResponse>() {
                    @Override
                    public void onSuccess(NewReceiveSaveResponse value) {
                        newReceiveSaveResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        newReceiveSaveResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }

    public void receiveNumbers(int id) {
        disposable.add(networkRepository.receiveNumbers(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> receiveNumbersResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<ReceiveNumbersResponse>() {
                    @Override
                    public void onSuccess(ReceiveNumbersResponse value) {
                        receiveNumbersResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        receiveNumbersResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }


    public void receiveItems(String receiveNo, String startDate, String endDate, int merchantId) {
        disposable.add(networkRepository.receiveItems(receiveNo, startDate, endDate, merchantId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> receiveItemsResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<ReceiveItemsResponse>() {
                    @Override
                    public void onSuccess(ReceiveItemsResponse value) {
                        receiveItemsResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        receiveItemsResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }

    public void receiveItemView(String receiveNo, String receiverName) {
        disposable.add(networkRepository.receiveItemView(receiveNo, receiverName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> receiveItemViewResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<ReceiveItemViewResponse>() {
                    @Override
                    public void onSuccess(ReceiveItemViewResponse value) {
                        receiveItemViewResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        receiveItemViewResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }
}
