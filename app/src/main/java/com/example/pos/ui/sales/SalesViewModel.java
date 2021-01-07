package com.example.pos.ui.sales;

import com.example.pos.model.DeleteSaleItemResponse;
import com.example.pos.model.GenerateSaleNoResponse;
import com.example.pos.model.PreSaleResponse;
import com.example.pos.model.SaleNumbersResponse;
import com.example.pos.model.SaleOrderDetailResponse;
import com.example.pos.model.SaleOrdersSaveResponse;
import com.example.pos.model.SaleOrdersSearchResponse;
import com.example.pos.model.UOMResponse;
import com.example.pos.model.UpdateSaleItemResponse;
import com.example.pos.network.ApiResponse;
import com.example.pos.network.NetworkRepository;
import com.example.pos.ui.base.BaseViewModel;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SalesViewModel extends BaseViewModel {
    private NetworkRepository networkRepository;

    @Inject
    public SalesViewModel(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

    public void UOM(String itemCode, int id) {
        disposable.add(networkRepository.UOM(itemCode, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> uomResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<UOMResponse>() {
                    @Override
                    public void onSuccess(UOMResponse value) {
                        uomResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        uomResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }

    public void generateSaleNo() {
        disposable.add(networkRepository.generateSaleNo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> generateSaleNoResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<GenerateSaleNoResponse>() {
                    @Override
                    public void onSuccess(GenerateSaleNoResponse value) {
                        generateSaleNoResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        generateSaleNoResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }

    public void preSale(String itemCode, int uomId, int merchantId, int qty) {
        disposable.add(networkRepository.preSale(itemCode, uomId, merchantId, qty)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> preSaleResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<PreSaleResponse>() {
                    @Override
                    public void onSuccess(PreSaleResponse value) {
                        preSaleResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        preSaleResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }

    public void saleOrdersSave(Map<String, Object> body) {
        disposable.add(networkRepository.saleOrdersSave(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> saleOrdersSaveResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<SaleOrdersSaveResponse>() {
                    @Override
                    public void onSuccess(SaleOrdersSaveResponse value) {
                        saleOrdersSaveResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        saleOrdersSaveResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }

    public void saleOrdersSearch(String saleNo, String startDate, String endDate, int id) {
        disposable.add(networkRepository.saleOrdersSearch(saleNo, startDate, endDate, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> saleOrdersSearchResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<SaleOrdersSearchResponse>() {
                    @Override
                    public void onSuccess(SaleOrdersSearchResponse value) {
                        saleOrdersSearchResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        saleOrdersSearchResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }

    public void saleNumbers(int id) {
        disposable.add(networkRepository.saleNumbers(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> saleNumbersResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<SaleNumbersResponse>() {
                    @Override
                    public void onSuccess(SaleNumbersResponse value) {
                        saleNumbersResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        saleNumbersResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }

    public void saleOrderDetail(String receiveNo, double price) {
        disposable.add(networkRepository.saleOrdersDatail(receiveNo, price)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> saleOrderDetailResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<SaleOrderDetailResponse>() {
                    @Override
                    public void onSuccess(SaleOrderDetailResponse value) {
                        saleOrderDetailResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        saleOrderDetailResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }

    public void deleteSaleItem(int id, String itemCode, int uomId, int merchantId, int qty, String salesNo, int isLast) {
        disposable.add(networkRepository.deleteSaleItem(id, itemCode, uomId, merchantId, qty, salesNo, isLast)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> deleteSaleItemResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<DeleteSaleItemResponse>() {
                    @Override
                    public void onSuccess(DeleteSaleItemResponse value) {
                        deleteSaleItemResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        deleteSaleItemResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }

    public void updateSaleItem(int id, String itemCode, int uomId, int merchantId, int qty, String salesNo) {
        disposable.add(networkRepository.updateSaleItem(id, itemCode, uomId, merchantId, qty, salesNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> updateSaleItemResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<UpdateSaleItemResponse>() {
                    @Override
                    public void onSuccess(UpdateSaleItemResponse value) {
                        updateSaleItemResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        updateSaleItemResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }

}
