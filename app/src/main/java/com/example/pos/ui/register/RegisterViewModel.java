package com.example.pos.ui.register;

import com.example.pos.model.NRCFormatResponse;
import com.example.pos.model.StateCityResponse;
import com.example.pos.network.ApiResponse;
import com.example.pos.network.NetworkRepository;
import com.example.pos.ui.base.BaseViewModel;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class RegisterViewModel extends BaseViewModel {
    private NetworkRepository networkRepository;

    @Inject
    RegisterViewModel(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

//    public void register(Map<String, Object> body) {
//        disposable.add(networkRepository.register(body)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(__ -> registerResponse.setValue(ApiResponse.loading()))
//                .subscribeWith(new DisposableSingleObserver<RegisterResponse>() {
//                    @Override
//                    public void onSuccess(RegisterResponse value) {
//                        registerResponse.setValue(ApiResponse.success(value));
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        registerResponse.setValue(ApiResponse.error(getErrorMessage(e)));
//                    }
//                }));
//    }


    public void statesTownships() {
        disposable.add(networkRepository.stateCity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> statesTownshipsResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<StateCityResponse>() {
                    @Override
                    public void onSuccess(StateCityResponse value) {
                        statesTownshipsResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        statesTownshipsResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }

    public void nrcFormat(Map<String, Object> body) {
        disposable.add(networkRepository.nrcFormat(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> nrcFormatResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<NRCFormatResponse>() {
                    @Override
                    public void onSuccess(NRCFormatResponse value) {
                        nrcFormatResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        nrcFormatResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }
}
