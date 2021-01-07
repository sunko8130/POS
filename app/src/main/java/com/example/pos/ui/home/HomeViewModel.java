package com.example.pos.ui.home;

import com.example.pos.model.BalanceResponse;
import com.example.pos.network.ApiResponse;
import com.example.pos.network.NetworkRepository;
import com.example.pos.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends BaseViewModel {
    private NetworkRepository networkRepository;

    @Inject
    public HomeViewModel(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

    public void balance(int id) {
        disposable.add(networkRepository.balance(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> balanceResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<BalanceResponse>() {
                    @Override
                    public void onSuccess(BalanceResponse value) {
                        balanceResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        balanceResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }
}
