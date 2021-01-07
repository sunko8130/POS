package com.example.pos.ui.login;

import com.example.pos.model.LoginResponse;
import com.example.pos.network.ApiResponse;
import com.example.pos.network.NetworkRepository;
import com.example.pos.ui.base.BaseViewModel;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends BaseViewModel {
    private NetworkRepository networkRepository;

    @Inject
    public LoginViewModel(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

    public void login(Map<String, Object> fields) {
        disposable.add(networkRepository.login(fields)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> loginResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<LoginResponse>() {
                    @Override
                    public void onSuccess(LoginResponse value) {
                        loginResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        loginResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }

//    public void login(String loginName, String loginPassword) {
//        disposable.add(networkRepository.login(loginName,loginPassword)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(__ -> loginResponse.setValue(ApiResponse.loading()))
//                .subscribeWith(new DisposableSingleObserver<LoginResponse>() {
//                    @Override
//                    public void onSuccess(LoginResponse value) {
//                        loginResponse.setValue(ApiResponse.success(value));
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        loginResponse.setValue(ApiResponse.error(getErrorMessage(e)));
//                    }
//                }));
//    }
}
