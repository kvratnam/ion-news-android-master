package com.mantra.ionnews.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mantra.ionnews.R;
import com.mantra.ionnews.datahandlers.ForgotPasswordDataHandler;
import com.mantra.ionnews.datahandlers.SignInDataHandler;
import com.mantra.ionnews.interfaces.BaseResponseInterface;
import com.mantra.ionnews.interfaces.OnForgotPasswordResponseListener;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.restclient.RequestBuilder;
import com.mantra.ionnews.ui.activities.DashboardActivity;
import com.mantra.ionnews.utils.Util;
import com.mantra.ionnews.utils.Validator;

import static com.mantra.ionnews.utils.ConstantClass.SIGN_IN;

/**
 * Created by rajat on 16/03/17.
 */

public class SignInBottomSheetDialog extends BaseBottomSheetDialog
        implements View.OnClickListener, BaseResponseInterface, OnForgotPasswordResponseListener {

    private String TAG = SIGN_IN;

    private RelativeLayout mainView, forgotPasswordView;
    private TextView tvSignIn, forgotPassword, tvForgotPasswordProceed, tvForgotPasswordAlert;
    private EditText etEmail, etPassword, etForgotPasswordEmail;
    private TextInputLayout textInputLayoutForgotPasswordEmailSec;

    private boolean isForgotPasswordViewVisible = false;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        Log.d(TAG, "onBackPress 3");
                        return true;
                    } else {
                        Log.d(TAG, "onBackPress 1");
                        if (isForgotPasswordViewVisible) removeForgotPasswordView();
                        else dismiss();

                        return true;
                    }
                } else {
                    Log.d(TAG, "onBackPress 2");
                    return false; // pass on to be processed as normal
                }
            }
        });
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_sign_in, null);
        initView(contentView);
        dialog.setContentView(contentView);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        ((BottomSheetBehavior) behavior).setPeekHeight(1000);

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    private void initView(View contentView) {
        tvSignIn = (TextView) contentView.findViewById(R.id.fsi_sign_in);
        etEmail = (EditText) contentView.findViewById(R.id.fsi_email);
        etPassword = (EditText) contentView.findViewById(R.id.fsi_password);
        mainView = (RelativeLayout) contentView.findViewById(R.id.fsi_main_view);
        forgotPassword = (TextView) contentView.findViewById(R.id.fsi_forgot_password);
        forgotPasswordView = (RelativeLayout) contentView.findViewById(R.id.fsi_forgot_password_view);
        etForgotPasswordEmail = (EditText) contentView.findViewById(R.id.cfp_email);
        tvForgotPasswordProceed = (TextView) contentView.findViewById(R.id.cfp_next);
        tvForgotPasswordAlert = (TextView) contentView.findViewById(R.id.cfp_check_mail_alert);
        textInputLayoutForgotPasswordEmailSec = (TextInputLayout) contentView.findViewById(R.id.cfp_email_sec);

        forgotPasswordView.setVisibility(View.GONE);

        tvSignIn.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        tvForgotPasswordProceed.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fsi_sign_in:
                signInUser();
                break;
            case R.id.fsi_forgot_password:
                onForgotPasswordClick();
                break;
            case R.id.cfp_next:
                onForgotPasswordProceedClick();
                break;
        }
    }

    private void onForgotPasswordProceedClick() {
        final String forgotPasswordEmailInput = etForgotPasswordEmail.getText().toString().trim();
        if (!Validator.isValidEmail(forgotPasswordEmailInput)) {
            etForgotPasswordEmail.requestFocus();
            etForgotPasswordEmail.setError(getString(R.string.error_invalid_email));
        } else {
            hideSoftKeyboard(etForgotPasswordEmail);
            initForgotPassword(forgotPasswordEmailInput);
        }
    }

    private void onForgotPasswordClick() {
        textInputLayoutForgotPasswordEmailSec.setVisibility(View.VISIBLE);
        tvForgotPasswordProceed.setVisibility(View.VISIBLE);
        tvForgotPasswordAlert.setVisibility(View.GONE);

        mainView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    mainView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                else
                    mainView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int mainViewHeight = mainView.getMeasuredHeight();
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) forgotPasswordView.getLayoutParams();
                layoutParams.height = mainViewHeight;
                forgotPasswordView.setLayoutParams(layoutParams);
            }
        });

        slideForgotPasswordView();
    }

    private void slideForgotPasswordView() {
        etEmail.setError(null);
        etPassword.setError(null);

        isForgotPasswordViewVisible = true;

        forgotPasswordView.setVisibility(View.VISIBLE);
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 1,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 0
        );
        animation.setDuration(300);
        forgotPasswordView.startAnimation(animation);
    }

    private void removeForgotPasswordView() {
        etForgotPasswordEmail.setError(null);

        isForgotPasswordViewVisible = false;

        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 0
        );
        animation.setDuration(300);
        forgotPasswordView.startAnimation(animation);
        forgotPasswordView.setVisibility(View.GONE);
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
        if (Util.hasInternetAccess(getContext()))
            performSignIn(
                    etEmail.getEditableText().toString().trim(),
                    etPassword.getEditableText().toString().trim()
            );
        else showNetworkErrorMessage(
                null,
                null,
                getContext()
        );
    }

    private void initForgotPassword(final String emailInput) {
        if (Util.hasInternetAccess(getContext()))
            performForgotPassword(emailInput);
        else showNetworkErrorMessage(
                null,
                null,
                getContext()
        );
    }

    private void performForgotPassword(String emailInput) {
        showProgressDialog(getString(R.string.loading));
        ForgotPasswordDataHandler forgotPasswordDataHandler = new ForgotPasswordDataHandler(this, getActivity());
        forgotPasswordDataHandler.request(RequestBuilder.getForgotPasswordRequest(emailInput));
    }

    private void performSignIn(String email, String password) {
        String deviceId = FirebaseInstanceId.getInstance().getToken();

        showProgressDialog(getString(R.string.signing_in));
        SignInDataHandler signInDataHandler = new SignInDataHandler(this, getActivity());
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
        dismiss();
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        startActivity(intent);
        getActivity().finish();
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
        textInputLayoutForgotPasswordEmailSec.setVisibility(View.GONE);
        tvForgotPasswordProceed.setVisibility(View.GONE);
        tvForgotPasswordAlert.setVisibility(View.VISIBLE);
        etForgotPasswordEmail.setText("");
    }
}
