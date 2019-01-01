package com.mantra.ionnews.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mantra.ionnews.R;
import com.mantra.ionnews.datahandlers.ForgotPasswordDataHandler;
import com.mantra.ionnews.interfaces.OnForgotPasswordResponseListener;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.restclient.RequestBuilder;
import com.mantra.ionnews.ui.dialogs.UtilDialogs;
import com.mantra.ionnews.utils.Util;
import com.mantra.ionnews.utils.Validator;


public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener,OnForgotPasswordResponseListener {

    private RelativeLayout mainView, forgotPasswordView;
    private TextView tvSignIn, forgotPassword, tvForgotPasswordProceed, tvForgotPasswordAlert;
    private EditText etEmail, etPassword, etForgotPasswordEmail;
    private TextInputLayout textInputLayoutForgotPasswordEmailSec;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cfp_next:
                onForgotPasswordProceedClick();
                break;
        }

    }
    private void initView() {

        etForgotPasswordEmail = (EditText)findViewById(R.id.cfp_email);
        tvForgotPasswordProceed = (TextView) findViewById(R.id.cfp_next);
        tvForgotPasswordAlert = (TextView) findViewById(R.id.cfp_check_mail_alert);
        textInputLayoutForgotPasswordEmailSec = (TextInputLayout) findViewById(R.id.cfp_email_sec);


        tvForgotPasswordProceed.setOnClickListener(this);

    }

        private void onForgotPasswordProceedClick() {
        final String forgotPasswordEmailInput = etForgotPasswordEmail.getText().toString().trim();
        if (!Validator.isValidEmail(forgotPasswordEmailInput)) {
            etForgotPasswordEmail.requestFocus();
            etForgotPasswordEmail.setError(getString(R.string.error_invalid_email));
        } else {
           // hideSoftKeyboard(ForgotPasswordActivity.this);
            initForgotPassword(forgotPasswordEmailInput);
        }
    }



    @Override
    public void onResume() {
        super.onResume();

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
        UtilDialogs utilDialogs= new UtilDialogs();
        utilDialogs.showSingleButtonDialog(ForgotPasswordActivity.this,getResources().getString(R.string.forgot_password),getResources().getString(R.string.forgot_password_check_mail_alert),getResources().getString(R.string.okay));

    }


    private void initForgotPassword(final String emailInput) {
        if (Util.hasInternetAccess(getApplicationContext()))
            performForgotPassword(emailInput);
        else showNetworkErrorMessage(
                null,
                null,
                getApplicationContext()
        );
    }

    private void performForgotPassword(String emailInput) {
        showProgressDialog(getString(R.string.loading));
        ForgotPasswordDataHandler forgotPasswordDataHandler = new ForgotPasswordDataHandler(this, getApplicationContext());
        forgotPasswordDataHandler.request(RequestBuilder.getForgotPasswordRequest(emailInput));

    }

}
