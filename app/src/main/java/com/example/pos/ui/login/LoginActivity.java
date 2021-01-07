package com.example.pos.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.pos.R;
import com.example.pos.common.ViewModelFactory;
import com.example.pos.model.LoginData;
import com.example.pos.model.LoginResponse;
import com.example.pos.network.ApiResponse;
import com.example.pos.ui.base.BaseActivity;
import com.example.pos.ui.home.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

import static com.example.pos.util.Constant.ALREADY_LOGIN;
import static com.example.pos.util.Constant.LOGIN_DATA;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_login_name)
    TextInputEditText etLoginName;

    @BindView(R.id.et_password)
    TextInputEditText etPassword;

    @Inject
    ViewModelFactory viewModelFactory;

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);

        // making notification bar transparent
        changeStatusBarColor();

        // Check if user is already logged in
        checkLoggedIn();

        //observe login data
        observeLogin();
    }

    private void checkLoggedIn() {
        boolean alreadyLogin = Paper.book().read(ALREADY_LOGIN, false);
        if (alreadyLogin) {
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);
            finish();
        }
    }

    private void observeLogin() {
        loginViewModel.loginResponse.observe(this, this::consumeLogin);
    }

    private void consumeLogin(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                LoginResponse loginResponse = (LoginResponse) apiResponse.data;
                if (loginResponse != null) {
                    int statusCode = loginResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        //save login data
                        LoginData loginData = loginResponse.getData();
                        Paper.book().write(LOGIN_DATA, loginData);
                        Paper.book().write(ALREADY_LOGIN, true);
                        Intent mainIntent = new Intent(this, MainActivity.class);
                        startActivity(mainIntent);
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(loginResponse.getStatus().getMessage());
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

    @OnClick(R.id.btn_login)
    void btnLogin() {
        if (TextUtils.isEmpty(etLoginName.getText())) {
            etLoginName.setError(getString(R.string.require_login_name));
        } else if (TextUtils.isEmpty(etPassword.getText())) {
            etPassword.setError(getString(R.string.require_login_password));
        } else {
            String loginName = etLoginName.getText().toString().trim();
            String loginPassword = etPassword.getText().toString().trim();
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("username", loginName);
            parameters.put("password", loginPassword);
            loginViewModel.login(parameters);
//            loginViewModel.login(loginName, loginPassword);
        }
    }

    @OnClick(R.id.tv_forgot_password)
    void forgotPassword() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //Making notification bar transparent
    private void changeStatusBarColor() {

        //make translucent statusBar on kitkat devices
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}