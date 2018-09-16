package com.mantra.ionnews.ui.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mantra.ionnews.R;
import com.mantra.ionnews.datahandlers.SignUpDataHandler;
import com.mantra.ionnews.interfaces.BaseResponseInterface;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.restclient.RequestBuilder;
import com.mantra.ionnews.ui.activities.DashboardActivity;
import com.mantra.ionnews.utils.Util;
import com.mantra.ionnews.utils.Validator;

import static com.mantra.ionnews.utils.ConstantClass.SIGN_UP;

/**
 * Created by rajat on 16/03/17.
 */

public class SignUpBottomSheetDialog extends BaseBottomSheetDialog
        implements View.OnClickListener, BaseResponseInterface
//        , OnGetUserGroupResponseListener, OnUserGroupDialogClickListener
{

    private LinearLayout mainView;
    private TextView tvSignUp;
    private EditText etUsername, etPassword, etFirstName, etLastName, etPhoneNumber, etOrganization,
            etDesignation
//            , etUserGroup
                    ;
//    private TextInputLayout userGroupDropDown;

//    private UserGroupDialog userGroupDialog;
//    private List<UserGroup> userGroups = new ArrayList<>();
//    private String selectedUserGroup = null;
//    private int selectedUserGroupId = -1;

    private String TAG = SIGN_UP;

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
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_sign_up, null);
        initView(contentView);

        dialog.setContentView(contentView);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();

        CoordinatorLayout.Behavior behavior = params.getBehavior();
        ((BottomSheetBehavior) behavior).setPeekHeight(1000);

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

//        getAllUserGroups();
//        userGroupDialog = new UserGroupDialog(getContext(), this);
    }

//    private void getAllUserGroups() {
//        GetUserGroupDataHandler getUserGroupDataHandler = new GetUserGroupDataHandler(this, getContext());
//        if (Util.hasInternetAccess(getContext())) {
//            getUserGroupDataHandler.request();
//            showProgressDialog(getString(R.string.loading));
//        } else
//            showNetworkErrorMessage(
//                    null,
//                    null,
//                    getContext()
//            );
//    }

    private void initView(View contentView) {
        tvSignUp = (TextView) contentView.findViewById(R.id.fsu_sign_up);
        etUsername = (EditText) contentView.findViewById(R.id.fsu_email);
        etPassword = (EditText) contentView.findViewById(R.id.fsu_password);
        etFirstName = (EditText) contentView.findViewById(R.id.fsu_first_name);
        etLastName = (EditText) contentView.findViewById(R.id.fsu_last_name);
        etPhoneNumber = (EditText) contentView.findViewById(R.id.fsu_phone_number);
        etOrganization = (EditText) contentView.findViewById(R.id.fsu_organization);
        etDesignation = (EditText) contentView.findViewById(R.id.fsu_designation);
//        etUserGroup = (EditText) contentView.findViewById(R.id.fsu_user_group);
//        userGroupDropDown = (TextInputLayout) contentView.findViewById(R.id.fsu_user_group_drop_down);
        mainView = (LinearLayout) contentView.findViewById(R.id.fsu_main_view);

//        etUserGroup.setFocusableInTouchMode(false);
//        etUserGroup.setFocusable(false);

        tvSignUp.setOnClickListener(this);
//        userGroupDropDown.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showUserGroupDropDown();
//            }
//        });

//        etUserGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showUserGroupDropDown();
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fsu_sign_up:
                signUpUser();
                break;
        }
    }

//    private void showUserGroupDropDown() {
//        if (userGroups.size() > 0)
//            userGroupDialog.showUserGroupDropdown(userGroups);
//        else
//            getAllUserGroups();
//    }

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
//            } else if (!Validator.isValidMobileNumber(etPhoneNumber.getEditableText().toString())) {
//                etPhoneNumber.requestFocus();
//                etPhoneNumber.setError(getString(R.string.error_invalid_phone_number));
//                return;
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
        if (Util.hasInternetAccess(getContext()))
            performSignUp(etUsername.getEditableText().toString().trim(),
                    etFirstName.getEditableText().toString().trim(),
                    etLastName.getEditableText().toString().trim(),
                    etPassword.getEditableText().toString().trim(),
                    etPhoneNumber.getEditableText().toString().trim(),
                    etOrganization.getEditableText().toString().trim(),
                    etDesignation.getEditableText().toString().trim()
//                    ,
//                    selectedUserGroup, selectedUserGroupId
            );
        else
            showNetworkErrorMessage(
                    null,
                    null,
                    getContext()
            );
    }

    private void performSignUp(String email, String firstName, String lastName,
                               String password, String phone, String organization,
                               String designation
//            , String userGroup, int userGroupId
    ) {
        String deviceId = FirebaseInstanceId.getInstance().getToken();

        showProgressDialog(getString(R.string.signing_up));
        SignUpDataHandler signUpDataHandler = new SignUpDataHandler(this, getActivity());
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
        dismiss();
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

//    @Override
//    public void getUserGroupResponse(Object object, Error error) {
//        hideProgressDialog();
//        if (error == null) {
//            userGroups = (List<UserGroup>) object;
//        } else {
//            showToastMessage(error.getMessage(), false);
//            dismiss();
//        }
//    }
//
//    @Override
//    public void onUserGroupSelected(String userGroup, int userGroupId) {
//        etUserGroup.setText(userGroup);
//        selectedUserGroup = userGroup;
//        selectedUserGroupId = userGroupId;
//    }
}
