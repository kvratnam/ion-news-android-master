package com.mantra.ionnews.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mantra.ionnews.R;
import com.mantra.ionnews.datahandlers.GetUserGroupDataHandler;
import com.mantra.ionnews.datahandlers.SignUpDataHandler;
import com.mantra.ionnews.interfaces.BaseResponseInterface;
import com.mantra.ionnews.interfaces.OnGetUserGroupResponseListener;
import com.mantra.ionnews.interfaces.OnUserGroupDialogClickListener;
import com.mantra.ionnews.models.User;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.models.responses.UserGroup;
import com.mantra.ionnews.restclient.RequestBuilder;
import com.mantra.ionnews.ui.dialogs.UserGroupDialog;
import com.mantra.ionnews.utils.LocalStorage;
import com.mantra.ionnews.utils.Util;
import com.mantra.ionnews.utils.Validator;

import java.util.ArrayList;
import java.util.List;


public class SignupActivity extends BaseActivity implements View.OnClickListener, BaseResponseInterface,OnGetUserGroupResponseListener, OnUserGroupDialogClickListener
{

    private EditText etUsername, etPassword, etFirstName, etLastName, etPhoneNumber, etOrganization,
            etDesignation, etUserGroup;
    private LinearLayout mainView;
    TextView textViewSingUp;

    ;
    private TextInputLayout userGroupDropDown;

    private UserGroupDialog userGroupDialog;
    private List<UserGroup> userGroups = new ArrayList<>();
    private String selectedUserGroup = null;
    private int selectedUserGroupId = -1;
    private User user;
    ImageView imageView;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initView();


    }

    private void initView() {


        etUsername = (EditText) findViewById(R.id.fsu_email);
        etPassword = (EditText) findViewById(R.id.fsu_password);
        etFirstName = (EditText) findViewById(R.id.fsu_first_name);
        etLastName = (EditText) findViewById(R.id.fsu_last_name);
        etPhoneNumber = (EditText) findViewById(R.id.fsu_phone_number);
        etOrganization = (EditText) findViewById(R.id.fsu_organization);
        etDesignation = (EditText) findViewById(R.id.fsu_designation);
        etUserGroup = (EditText) findViewById(R.id.fsu_user_group);
        textViewSingUp = (TextView) findViewById(R.id.fsu_sign_up);
        textViewSingUp.setOnClickListener(this);
        userGroupDropDown = (TextInputLayout) findViewById(R.id.fsu_user_group_drop_down);
        mainView = (LinearLayout) findViewById(R.id.fsu_main_view);
        imageView = (ImageView) findViewById(R.id.inside_imageview);
        imageView.setAdjustViewBounds(true);

        disableEditTexts(etUserGroup);



      /* getAllUserGroups();

        user = LocalStorage.getInstance(getApplicationContext()).getUser();
        if(user.getRole() != null)
        {
            etUserGroup.setText(user.getRole().substring(0, 1).toUpperCase() + user.getRole().substring(1));
            selectedUserGroup = user.getRole();
            selectedUserGroupId = user.getRoleId();
        }


        userGroupDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserGroupDropDown();
            }
        });

        etUserGroup.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                showUserGroupDropDown();


           }
        });*/
    }



    private void getAllUserGroups() {
        GetUserGroupDataHandler getUserGroupDataHandler = new GetUserGroupDataHandler(this, getApplicationContext());
        if (Util.hasInternetAccess(getApplicationContext())) {
            getUserGroupDataHandler.request();
           // showProgressDialog(getString(R.string.loading));
        }
        /*else
            showNetworkErrorMessage(
                    null,
                    null,
                    getApplicationContext()
            );*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fsu_sign_up:
                signUpUser();
                break;
        }
    }

    private void signUpUser() {
        isValidData();

    }

    public void isValidData() {
        try {
            if (!Validator.isValidName(etFirstName.getEditableText().toString())) {
                etFirstName.requestFocus();
                etFirstName.setError(getString(R.string.error_invalid_name));
                return;
            } else if (!Validator.isValidName(etLastName.getEditableText().toString())) {
                etLastName.requestFocus();
                etLastName.setError(getString(R.string.error_invalid_name));
                return;
            } else if (!Validator.isValidEmail(etUsername.getEditableText().toString())) {
                etUsername.requestFocus();
                etUsername.setError(getString(R.string.error_invalid_email));
                return;
            } else if (!Validator.isValidPassword(etPassword.getEditableText().toString())) {
                etPassword.requestFocus();
                etPassword.setError(getString(R.string.error_invalid_password));
                return;
           } else if (!Validator.isValidMobileNumber(etPhoneNumber.getEditableText().toString())) {
                etPhoneNumber.requestFocus();
                etPhoneNumber.setError(getString(R.string.error_invalid_phone_number));
                return;
//            } else if (selectedUserGroup == null) {
//                etUserGroup.requestFocus();
//                etUserGroup.setError(getString(R.string.error_invalid_user_group));
//                return;
            } else initSignUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSignUp() {
        if (Util.hasInternetAccess(getApplicationContext()))
            performSignUp(etUsername.getEditableText().toString().trim(),
                    etFirstName.getEditableText().toString().trim(),
                    etLastName.getEditableText().toString().trim(),
                    etPassword.getEditableText().toString().trim(),
                    etPhoneNumber.getEditableText().toString().trim(),
                    etOrganization.getEditableText().toString().trim(),
                    etDesignation.getEditableText().toString().trim(),
                    selectedUserGroup, selectedUserGroupId
            );
        else
            showNetworkErrorMessage(
                    null,
                    null,
                    getApplicationContext()
            );
    }

    private void performSignUp(String email, String firstName, String lastName,
                               String password, String phone, String organization,
                               String designation
            , String userGroup, int userGroupId
    ) {
        String deviceId = FirebaseInstanceId.getInstance().getToken();

        showProgressDialog(getString(R.string.signing_up));
        SignUpDataHandler signUpDataHandler = new SignUpDataHandler(this, getApplicationContext());
        signUpDataHandler.request(RequestBuilder.getSignUpRequest(email, firstName, lastName,
                password, phone, organization, designation, deviceId
//                , userGroup, userGroupId
        ));
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
       // dismiss();
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(intent);
        finish();
    }




    private void disableEditTexts(EditText mEditText) {
        mEditText.setClickable(false);
        mEditText.setFocusable(false);
        mEditText.setFocusableInTouchMode(false);
    }



    private void showUserGroupDropDown() {
       if (userGroups.size() > 0)
            userGroupDialog.showUserGroupDropdown(userGroups);
        else
           getAllUserGroups();
    }

        @Override
   public void getUserGroupResponse(Object object, Error error) {
       // hideProgressDialog();
        if (error == null) {
            userGroups = (List<UserGroup>) object;
        } else {
           /* showToastMessage(error.getMessage(), false);
            dismiss();*/
        }
    }

    @Override
    public void onUserGroupSelected(String userGroup, int userGroupId) {
        etUserGroup.setText(userGroup);
        selectedUserGroup = userGroup;
        selectedUserGroupId = userGroupId;
    }

}
