package com.example.pos.ui.pricing;

import com.example.pos.model.ItemsPriceResponse;
import com.example.pos.model.MerchantItemsResponse;
import com.example.pos.model.SetSellingPriceResponse;
import com.example.pos.network.ApiResponse;
import com.example.pos.network.NetworkRepository;
import com.example.pos.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class PricingViewModel extends BaseViewModel {
    private NetworkRepository networkRepository;

    @Inject
    public PricingViewModel(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

    public void merchantItems(int id) {
        disposable.add(networkRepository.merchantItems(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> merchantItemsResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<MerchantItemsResponse>() {
                    @Override
                    public void onSuccess(MerchantItemsResponse value) {
                        merchantItemsResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        merchantItemsResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }

    public void itemsPriceSearch(String itemCode,int id) {
        disposable.add(networkRepository.itemsPriceSearch(itemCode,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> itemsPriceResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<ItemsPriceResponse>() {
                    @Override
                    public void onSuccess(ItemsPriceResponse value) {
                        itemsPriceResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        itemsPriceResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }

    public void setSellingPrice(int id, double price) {
        disposable.add(networkRepository.setSellingPrice(id, price)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> setSellingPriceResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<SetSellingPriceResponse>() {
                    @Override
                    public void onSuccess(SetSellingPriceResponse value) {
                        setSellingPriceResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        setSellingPriceResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }
}
