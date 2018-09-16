package com.mantra.ionnews.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.TextView;

import com.mantra.ionnews.R;
import com.mantra.ionnews.ui.dialogs.SignInBottomSheetDialog;
import com.mantra.ionnews.ui.dialogs.SignUpBottomSheetDialog;
import com.mantra.ionnews.utils.LocalStorage;

import static com.mantra.ionnews.utils.ConstantClass.ONBOARDING;

/**
 * Created by rajat on 16/03/17.
 */

public class OnboardingActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = ONBOARDING;
    private TextView tvLogIn;
    private TextView tvSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        if (LocalStorage.getInstance(getApplicationContext()).getUser() == null) {
            initView();
        } else {
            gotoDashboard();
        }
    }

    private void gotoDashboard() {
        Intent intent = new Intent(OnboardingActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    private void initView() {
        tvLogIn = (TextView) findViewById(R.id.ao_log_in);
        tvLogIn.setOnClickListener(this);

        tvSignUp = (TextView) findViewById(R.id.ao_sign_up);
        tvSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ao_log_in:
                openLoginDialog();
                break;
            case R.id.ao_sign_up:
                openSignUpDialog();
                break;
        }
    }

    private void openSignUpDialog() {
        BottomSheetDialogFragment bottomSheetDialogFragment = new SignUpBottomSheetDialog();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    private void openLoginDialog() {
        BottomSheetDialogFragment bottomSheetDialogFragment = new SignInBottomSheetDialog();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }
}
