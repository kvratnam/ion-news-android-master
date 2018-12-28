package com.mantra.ionnews.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mantra.ionnews.R;
import com.mantra.ionnews.datahandlers.ForgotPasswordDataHandler;
import com.mantra.ionnews.datahandlers.SignInDataHandler;
import com.mantra.ionnews.interfaces.BaseResponseInterface;
import com.mantra.ionnews.interfaces.OnForgotPasswordResponseListener;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.restclient.RequestBuilder;
import com.mantra.ionnews.ui.dialogs.BaseBottomSheetDialog;
import com.mantra.ionnews.utils.LocalStorage;
import com.mantra.ionnews.utils.Util;
import com.mantra.ionnews.utils.Validator;


public class LoginActivity extends BaseActivity implements View.OnClickListener, BaseResponseInterface, OnForgotPasswordResponseListener {
    private EditText etEmail, etPassword;
    Button buttonSignIn;
    private TextView tvSignUp;
    private TextView tvForgotPassword;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initView();
        if (LocalStorage.getInstance(getApplicationContext()).getUser() == null) {
            initView();
        } else {
            gotoDashboard();
        }

    }


    private void gotoDashboard() {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }



    private void initView() {
        etEmail = (EditText) findViewById(R.id.fsi_email);
        etPassword = (EditText) findViewById(R.id.fsi_password);
        buttonSignIn = (Button) findViewById(R.id.fsi_sign_in);
        tvSignUp = (TextView) findViewById(R.id.ao_sign_up);
        tvForgotPassword = (TextView) findViewById(R.id.cfp_email);
        tvForgotPassword.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);

        etEmail.setText("ramasamy.vcp@gmail.com");
        etPassword.setText("123456");




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fsi_sign_in:
                signInUser();
                break;
            case R.id.ao_sign_up:
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                break;
            case R.id.cfp_email:
                performForgotPassword(etEmail.getText().toString());
                break;


        }
    }

    private void signInUser() {
        isValidData();

    }

    public void isValidData() {
        try {
            if (!Validator.isValidEmail(etEmail.getEditableText().toString())) {
                etEmail.requestFocus();
                etEmail.setError(getString(R.string.error_invalid_email));
                return;
            } else if (!Validator.isValidPassword(etPassword.getEditableText().toString())) {
                etPassword.requestFocus();
                etPassword.setError(getString(R.string.error_invalid_password));
                return;
            } else
                initSignIn();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSignIn() {
        if (Util.hasInternetAccess(getApplicationContext()))
            performSignIn(
                    etEmail.getEditableText().toString().trim(),
                    etPassword.getEditableText().toString().trim()
            );
        else showNetworkErrorMessage(

                null,
                null,
                getApplicationContext());
    }

    private void performSignIn(String email, String password) {
        String deviceId = FirebaseInstanceId.getInstance().getToken();

        showProgressDialog(getString(R.string.signing_in));
        SignInDataHandler signInDataHandler = new SignInDataHandler(this, getApplicationContext());
        signInDataHandler.request(RequestBuilder.getSignInRequest(email, password, deviceId));

    }


    @Override
    public void response(Object response, Error error) {
        hideProgressDialog();
        if (error == null) {
            proceedToNextView();
        } else {
            showToastMessage(error.getMessage(), false);
        }

    }

    private void proceedToNextView() {
         //  dismiss();
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private void performForgotPassword(String emailInput) {
        showProgressDialog(getString(R.string.loading));
        ForgotPasswordDataHandler forgotPasswordDataHandler = new ForgotPasswordDataHandler(this, getApplicationContext());
        forgotPasswordDataHandler.request(RequestBuilder.getForgotPasswordRequest(emailInput));
    }

    @Override
    public void forgotPasswordResponse(String message, Error error) {
        hideProgressDialog();
        if (error == null) {
            onForgotPasswordSuccess();
        } else {
            showToastMessage(error.getMessage(), false);
        }
    }

    private void onForgotPasswordSuccess() {

    }
}
