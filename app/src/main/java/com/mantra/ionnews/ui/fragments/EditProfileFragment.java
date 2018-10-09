package com.mantra.ionnews.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mantra.ionnews.R;
import com.mantra.ionnews.datahandlers.EditProfileDataHandler;
import com.mantra.ionnews.datahandlers.GetUserGroupDataHandler;
import com.mantra.ionnews.datahandlers.ProfileImageUploadDataHandler;
import com.mantra.ionnews.interfaces.BaseResponseInterface;
import com.mantra.ionnews.interfaces.OnGetUserGroupResponseListener;
import com.mantra.ionnews.interfaces.OnImageUploadResponseListener;
import com.mantra.ionnews.interfaces.OnProfileUpdateListener;
import com.mantra.ionnews.interfaces.OnUserGroupDialogClickListener;
import com.mantra.ionnews.models.FragmentState;
import com.mantra.ionnews.models.User;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.models.responses.UserGroup;
import com.mantra.ionnews.restclient.RequestBuilder;
import com.mantra.ionnews.ui.dialogs.UserGroupDialog;
import com.mantra.ionnews.utils.LocalStorage;
import com.mantra.ionnews.utils.Util;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.mantra.ionnews.utils.ConstantClass.EDIT_PROFILE;

/**
 * Created by TaNMay on 03/05/17.
 */

public class EditProfileFragment extends BaseFragment
        implements BaseResponseInterface, OnImageUploadResponseListener, OnUserGroupDialogClickListener, OnGetUserGroupResponseListener {

    private static final String ARG_PARAM1 = "ARG_PARAM1";
    private static final int PICK_IMAGE = 1001;

    private String TAG = EDIT_PROFILE + " ==>";

    private RelativeLayout profileImageEditSec, mainView;
    private ImageView profileImage, back;
    private TextView save;
    private EditText etFirstName, etLastName, etEmail, etPhoneNumber, etOrganization, etDesignation,
            etUserGroup;
    private TextInputLayout userGroupDropDown;

    private User user;
    private List<UserGroup> userGroups = new ArrayList<>();
    private String selectedUserGroup = null;
    private int selectedUserGroupId = -1;

    private boolean newProfileImg = false;

    private UserGroupDialog userGroupDialog;

    public EditProfileFragment() {

    }

    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
//        args.putParcelableArrayList(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        updateFragment();
        initView(layout);

        user = LocalStorage.getInstance(getContext()).getUser();

        setUpHeader();
        setUpUserProfile();

        getAllUserGroups();

        selectedUserGroup = user.getRole();
        selectedUserGroupId = user.getRoleId();
        userGroupDialog = new UserGroupDialog(getContext(), this);

        return layout;
    }

    private void getAllUserGroups() {
        GetUserGroupDataHandler getUserGroupDataHandler = new GetUserGroupDataHandler(this, getContext());
        if (Util.hasInternetAccess(getContext())) {
            getUserGroupDataHandler.request();
            showProgressDialog(getString(R.string.loading));
        } else
            showNetworkErrorMessage(
                    null,
                    null,
                    getContext()
            );
    }

    private void initView(View rootView) {
        etFirstName = (EditText) rootView.findViewById(R.id.bsep_first_name);
        etLastName = (EditText) rootView.findViewById(R.id.bsep_last_name);
        etEmail = (EditText) rootView.findViewById(R.id.bsep_email);
        etPhoneNumber = (EditText) rootView.findViewById(R.id.bsep_phone_number);
        etOrganization = (EditText) rootView.findViewById(R.id.bsep_organization);
        etDesignation = (EditText) rootView.findViewById(R.id.bsep_designation);
        etUserGroup = (EditText) rootView.findViewById(R.id.bsep_user_group);
        userGroupDropDown = (TextInputLayout) rootView.findViewById(R.id.bsep_user_group_dropdown);
        profileImageEditSec = (RelativeLayout) rootView.findViewById(R.id.bsep_image_sec);
        mainView = (RelativeLayout) rootView.findViewById(R.id.bsep_main_view);
        profileImage = (ImageView) rootView.findViewById(R.id.bsep_image);
        back = (ImageView) rootView.findViewById(R.id.bsep_back);
        save = (TextView) rootView.findViewById(R.id.bsep_save);

        disableEditTexts(etEmail);
      //  disableEditTexts(etPhoneNumber);
        disableEditTexts(etUserGroup);
    }

    private void updateFragment() {
        EventBus.getDefault().postSticky(new FragmentState(EDIT_PROFILE));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static OnProfileUpdateListener onProfileUpdateListener;
    @Override
    public void onDetach() {
        onProfileUpdateListener.onProfileUpdated();
        super.onDetach();
    }

    private void setUpHeader() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.hideSoftKeyboard(getActivity());
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileSaveClick();
            }
        });
    }

    private void onProfileSaveClick() {
        String newFirstName = etFirstName.getText().toString().trim();
        String newLastName = etLastName.getText().toString().trim();
        String newOrganization = etOrganization.getText().toString().trim();
        String newDesignation = etDesignation.getText().toString().trim();
        String newMobileNum = etPhoneNumber.getText().toString().trim();
        String newRole = selectedUserGroup;
        if (!user.getFirstName().equals(newFirstName)
                || !user.getLastName().equals(newLastName)
                || !user.getCompany().equals(newOrganization)
                || !user.getDesignation().equals(newDesignation)
                || !user.getPhone().equals(newMobileNum)
                || !user.getRole().equals(newRole)) {
            initEditProfileRequest(newFirstName, newLastName, newOrganization, newDesignation, newMobileNum,selectedUserGroup, selectedUserGroupId);
        } else if (newProfileImg) {
            showToastMessage(getString(R.string.saved), false);
            Util.hideSoftKeyboard(getActivity());
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            noChangeInProfile();
        }
    }

    private void noChangeInProfile() {
        showToastMessage(getString(R.string.no_change_in_profile), false);
        Util.hideSoftKeyboard(getActivity());
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void initEditProfileRequest(String firstName, String lastName, String organization,
                                        String designation, String mobile,String role, int roleId) {
        if (Util.hasInternetAccess(getContext())) {
            EditProfileDataHandler editProfileDataHandler = new EditProfileDataHandler(this, getContext());
            editProfileDataHandler.request(RequestBuilder.
                    getEditProfileRequest(firstName, lastName, organization, designation,mobile, role, roleId));
            showProgressDialog(getString(R.string.loading));
        } else {
            showNetworkErrorMessage(
                    null,
                    null,
                    getContext()
            );
        }
    }

    private void setUpUserProfile() {
        if (user.getFirstName() == null) user.setFirstName("");
        if (user.getLastName() == null) user.setLastName("");
        if (user.getCompany() == null) user.setCompany("");
        if (user.getDesignation() == null) user.setDesignation("");
        if (user.getPhone() == null) user.setPhone("");



        etFirstName.setText(user.getFirstName());
        etLastName.setText(user.getLastName());
        etEmail.setText(user.getEmail());
        etPhoneNumber.setText(user.getPhone());
        etOrganization.setText(user.getCompany());
        etDesignation.setText(user.getDesignation());
        etUserGroup.setText(user.getRole().substring(0, 1).toUpperCase() + user.getRole().substring(1));

        profileImageEditSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileImageEditClick();
            }
        });

        if (user.getProfileImg() != null)
            //setProfileImage(null, user.getProfileImg());
            setProfileImage(null, user.getProfileImg());

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
        });
    }

    private void showUserGroupDropDown() {
        if (userGroups.size() > 0)
            userGroupDialog.showUserGroupDropdown(userGroups);
        else
            getAllUserGroups();
    }

    private void onProfileImageEditClick() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap yourSelectedImage = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                showSnackbarMessage(mainView, getString(R.string.some_error_occured), false, null, null, false);
                return;
            } else {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();


                try {
                    yourSelectedImage = decodeUri(getContext(), selectedImage);
                    setProfileImage(yourSelectedImage, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setProfileImage(Bitmap yourSelectedImage, String url) {
        if (url == null || url.trim().isEmpty()) {
            //shruthi
            if(yourSelectedImage!=null){
                profileImage.setImageBitmap(yourSelectedImage);
                Bitmap squareImage = Util.getSquareBitmap(yourSelectedImage);
                sendImageToServer(squareImage);
            }

        } else {
            Picasso.with(getContext())
                    .load(url)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(profileImage);
        }
    }

    private Bitmap decodeUri(Context context, Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(selectedImage), null, o2);
    }

    private void sendImageToServer(Bitmap selectedImage) {
        String profileImageBase64 = getBitmapInBase64(selectedImage);

        ProfileImageUploadDataHandler profileImageUploadDataHandler = new ProfileImageUploadDataHandler(this, getContext());
        profileImageUploadDataHandler.request(RequestBuilder.getImageUploadRequest(profileImageBase64));
        showProgressDialog(getString(R.string.saving));
    }

    private String getBitmapInBase64(Bitmap selectedImage) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encoded;
    }

    private void disableEditTexts(EditText mEditText) {
        mEditText.setClickable(false);
        mEditText.setFocusable(false);
        mEditText.setFocusableInTouchMode(false);
    }

    @Override
    public void response(Object response, Error error) {
        hideProgressDialog();
        if (error == null) {
            User editProfileResponse = (User) response;
            user.setFirstName(editProfileResponse.getFirstName());
            user.setLastName(editProfileResponse.getLastName());
            user.setCompany(editProfileResponse.getCompany());
            user.setDesignation(editProfileResponse.getDesignation());
            user.setRole(editProfileResponse.getRole());
            user.setRoleId(editProfileResponse.getRoleId());

            LocalStorage.getInstance(getContext()).setUser(user);
            Activity activity = getActivity();
            if(activity != null){

                showToastMessage(getString(R.string.saved), false);
            }

            Util.hideSoftKeyboard(getActivity());
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            showToastMessage(error.getMessage(), false);
        }
    }

    @Override
    public void imageUploadResponse(Object object, Error error) {
        hideProgressDialog();
        if (error == null) {
            newProfileImg = true;
            /*todo upload image should get here*/
            user = LocalStorage.getInstance(getContext()).getUser();

            showToastMessage(getString(R.string.saved), false);
        } else {
            showToastMessage(error.getMessage(), false);
        }
    }

    @Override
    public void onUserGroupSelected(String userGroup, int userGroupId) {
        etUserGroup.setText(userGroup);
        selectedUserGroup = userGroup;
        selectedUserGroupId = userGroupId;
    }

    @Override
    public void getUserGroupResponse(Object object, Error error) {
        hideProgressDialog();
        if (error == null) {
            userGroups = (List<UserGroup>) object;
        } else {
            showToastMessage(error.getMessage(), false);
            Util.hideSoftKeyboard(getActivity());
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}