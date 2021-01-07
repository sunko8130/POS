package com.example.pos.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.pos.R;
import com.example.pos.common.ViewModelFactory;
import com.example.pos.model.NRCFormatResponse;
import com.example.pos.model.NRCNation;
import com.example.pos.model.NRCNo;
import com.example.pos.model.RegisterResponse;
import com.example.pos.model.State;
import com.example.pos.model.StateCityResponse;
import com.example.pos.model.Township;
import com.example.pos.network.ApiResponse;
import com.example.pos.ui.base.BaseActivity;
import com.example.pos.ui.login.LoginActivity;
import com.example.pos.widgets.MessageDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements MessageDialog.OnClickListener {
    @BindView(R.id.et_login_name)
    TextInputEditText etLoginName;

    @BindView(R.id.et_user_name)
    TextInputEditText etUserName;

    @BindView(R.id.et_store_name)
    TextInputEditText etStoreName;

    @BindView(R.id.et_owner_name)
    TextInputEditText etOwnerName;

    @BindView(R.id.et_address)
    TextInputEditText etAddress;

    @BindView(R.id.et_postal_code)
    TextInputEditText etPostalCode;

    @BindView(R.id.et_phone1)
    TextInputEditText etPhone1;

    @BindView(R.id.etPhone2)
    TextInputEditText etPhone2;

    @BindView(R.id.et_commission)
    TextInputEditText etCommission;

    @BindView(R.id.et_password)
    TextInputEditText etPassword;

    @BindView(R.id.et_confirm_password)
    TextInputEditText etConfirmPassword;

    @BindView(R.id.township_spinner)
    AppCompatSpinner spinnerTownship;

    @BindView(R.id.state_spinner)
    AppCompatSpinner spinnerState;

    @BindView(R.id.nrc_no_spinner)
    AppCompatSpinner spinnerNRCNo;

    @BindView(R.id.nrc_nation_spinner)
    AppCompatSpinner spinnerNRCNation;

    @BindView(R.id.nrc_nationality_spinner)
    AppCompatSpinner spinnerNRCNationality;

    @BindView(R.id.et_nrc_number)
    EditText etNRCNumber;

    String loginName, userName, storeName, ownerName, address, postalCode,
            contactPhone1, contactPhone2, comission, password, confirmPassword,
            nrcNationality;
    List<String> nrcNationalityList;
    ArrayAdapter<String> nrcNationalAdapter;

    @Inject
    ViewModelFactory viewModelFactory;

    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        //back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        registerViewModel = ViewModelProviders.of(this, viewModelFactory).get(RegisterViewModel.class);
        registerViewModel.statesTownships();

        nrcNationalityList = Arrays.asList(getResources().getStringArray(R.array.nrc_nationality));
        //nrc nationality spinner
        nrcNationalAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, R.id.spinnerText, nrcNationalityList);
        spinnerNRCNationality.setAdapter(nrcNationalAdapter);

        //observe statesTownships
        observeStatesTownships();
        //observe nrcFormat
        observeNRCFormat();
        //observe register
        observeRegister();
    }

    private void observeRegister() {
        registerViewModel.registerResponse.observe(this, this::consumeRegister);
    }

    private void consumeRegister(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                RegisterResponse registerResponse = (RegisterResponse) apiResponse.data;
                if (registerResponse != null) {
                    messageDialog.show();
                    messageDialog.setListener(this);
                    messageDialog.loadingMessage(registerResponse.getStatus().getMessage());
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

    private void observeStatesTownships() {
        registerViewModel.statesTownshipsResponse.observe(this, this::consumeStatesTownshipsResponse);
    }

    private void observeNRCFormat() {
        registerViewModel.nrcFormatResponse.observe(this, this::consumeNRCFormat);
    }

    private void consumeNRCFormat(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                NRCFormatResponse nrcFormatResponse = (NRCFormatResponse) apiResponse.data;
                if (nrcFormatResponse != null) {
                    int statusCode = nrcFormatResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        List<NRCNo> nrcNos = nrcFormatResponse.getData();

                        NRCNoAdapter nrcNoAdapter = new NRCNoAdapter(mActivity,
                                R.layout.dropdown_menu_popup_item, R.id.spinner_text, nrcNos);
                        spinnerNRCNo.setAdapter(nrcNoAdapter);
                        spinnerNRCNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                NRCNo nrcNo = nrcNoAdapter.getItem(i);
                                if (nrcNo != null) {
                                    List<NRCNation> nrcNations = nrcNo.getNrcNations();

                                    NRCNationAdapter nrcNationAdapter = new NRCNationAdapter(mActivity,
                                            R.layout.dropdown_menu_popup_item, R.id.spinner_text, nrcNations);
                                    spinnerNRCNation.setAdapter(nrcNationAdapter);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(nrcFormatResponse.getStatus().getMessage());
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

    private void consumeStatesTownshipsResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                //nrc format
                Map<String, Object> parameter = new HashMap<>();
                parameter.put("language", "en");
                registerViewModel.nrcFormat(parameter);

                StateCityResponse stateCityResponse = (StateCityResponse) apiResponse.data;
                if (stateCityResponse != null) {
                    int statusCode = stateCityResponse.getStatus().getCode();
                    if (statusCode == 1) {
                        List<State> states = stateCityResponse.getData();

                        StateAdapter stateAdapter = new StateAdapter(mActivity,
                                R.layout.dropdown_menu_popup_item, R.id.spinner_text, states);
                        spinnerState.setAdapter(stateAdapter);
                        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                State state = stateAdapter.getItem(i);
                                if (state != null) {
                                    List<Township> townships = state.getTownships();

                                    TownshipAdapter townshipAdapter = new TownshipAdapter(mActivity,
                                            R.layout.dropdown_menu_popup_item, R.id.spinner_text, townships);
                                    spinnerTownship.setAdapter(townshipAdapter);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(stateCityResponse.getStatus().getMessage());
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

    @OnClick(R.id.btn_register)
    void register() {

        loginName = etLoginName.getText().toString();
        userName = etUserName.getText().toString();
        storeName = etStoreName.getText().toString();
        ownerName = etOwnerName.getText().toString();
        address = etAddress.getText().toString();
        postalCode = etPostalCode.getText().toString();
        contactPhone1 = etPhone1.getText().toString();
        contactPhone2 = etPhone2.getText().toString();
        comission = etCommission.getText().toString();
        password = etPassword.getText().toString();
        postalCode = etPostalCode.getText().toString();
        confirmPassword = etConfirmPassword.getText().toString();
        NRCNo nrcNo = (NRCNo) spinnerNRCNo.getSelectedItem();
        NRCNation nrcNation = (NRCNation) spinnerNRCNation.getSelectedItem();
        nrcNationality = spinnerNRCNationality.getSelectedItem().toString();
        String nrc = nrcNo.getCode() + "/" + nrcNation.getNrc() + " " + nrcNationality + " " + etNRCNumber.getText().toString();
        State state = (State) spinnerState.getSelectedItem();
        int stateId = state.getId();
        Township township = (Township) spinnerTownship.getSelectedItem();
        int townshipId = township.getId();

        if (TextUtils.isEmpty(loginName)) {
            etLoginName.setError(getString(R.string.require_login_name));
        } else if (TextUtils.isEmpty(userName)) {
            etUserName.setError(getString(R.string.require_user_name));
        } else if (!password.equals(confirmPassword)) {
            etPassword.setError(getString(R.string.password_matcher));
            etConfirmPassword.setError(getString(R.string.password_matcher));
        } else if (TextUtils.isEmpty(password)) {
            etUserName.setError(getString(R.string.require_password));
        } else if (TextUtils.isEmpty(confirmPassword)) {
            etUserName.setError(getString(R.string.require_confirm_password));
        } else if (TextUtils.isEmpty(storeName)) {
            etUserName.setError(getString(R.string.require_store_name));
        } else if (TextUtils.isEmpty(ownerName)) {
            etUserName.setError(getString(R.string.require_owner_name));
        } else if (TextUtils.isEmpty(nrc)) {
            etUserName.setError(getString(R.string.require_nrc));
        } else if (TextUtils.isEmpty(contactPhone1)) {
            etUserName.setError(getString(R.string.require_phone1));
        } else if (TextUtils.isEmpty(address)) {
            etUserName.setError(getString(R.string.require_address));
        } else {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("userName", userName);
            parameters.put("userLoginName", loginName);
            parameters.put("password", password);
            parameters.put("commission", comission);
            parameters.put("storeName", storeName);
            parameters.put("ownerName", ownerName);
            parameters.put("nrc", nrc);
            parameters.put("address", address);
            parameters.put("stateId", stateId);
            parameters.put("townshipId", townshipId);
            parameters.put("postalCode", postalCode);
            parameters.put("contactPhone1", contactPhone1);
            parameters.put("contactPhone2", contactPhone2);
//            registerViewModel.register(parameters);
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
        messageDialog.dismiss();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}