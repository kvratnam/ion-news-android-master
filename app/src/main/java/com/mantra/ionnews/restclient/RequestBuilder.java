package com.mantra.ionnews.restclient;

import org.json.JSONException;
import org.json.JSONObject;

import static com.mantra.ionnews.utils.AppConstants.KEY_DESIGNATION;
import static com.mantra.ionnews.utils.AppConstants.KEY_DEVICE_CODE;
import static com.mantra.ionnews.utils.AppConstants.KEY_DEVICE_TYPE;
import static com.mantra.ionnews.utils.AppConstants.KEY_EMAIL;
import static com.mantra.ionnews.utils.AppConstants.KEY_FIRST_NAME;
import static com.mantra.ionnews.utils.AppConstants.KEY_LAST_NAME;
import static com.mantra.ionnews.utils.AppConstants.KEY_ORGANIZATION;
import static com.mantra.ionnews.utils.AppConstants.KEY_PASSWORD;
import static com.mantra.ionnews.utils.AppConstants.KEY_PHONE;
import static com.mantra.ionnews.utils.AppConstants.KEY_PROFILE_IMAGE;
import static com.mantra.ionnews.utils.AppConstants.KEY_ROLE;
import static com.mantra.ionnews.utils.AppConstants.KEY_ROLE_ID;
import static com.mantra.ionnews.utils.AppConstants.VAL_DEVICE_TYPE;
import static com.mantra.ionnews.utils.AppConstants.VAL_ROLE;
import static com.mantra.ionnews.utils.AppConstants.VAL_ROLE_ID;

/**
 * Created by TaNMay on 15/02/17.
 */

public class RequestBuilder {

    public static JSONObject getSignInRequest(String email, String password, String deviceCode) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(KEY_EMAIL, email);
            jsonObject.put(KEY_PASSWORD, password);
            jsonObject.put(KEY_DEVICE_CODE, deviceCode);
            jsonObject.put(KEY_DEVICE_TYPE, VAL_DEVICE_TYPE);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject getSignUpRequest(String email, String firstName, String lastName,
                                              String password, String phone, String organization,
                                              String designation, String deviceCode) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(KEY_EMAIL, email);
            jsonObject.put(KEY_FIRST_NAME, firstName);
            jsonObject.put(KEY_LAST_NAME, lastName);
            jsonObject.put(KEY_PASSWORD, password);
            jsonObject.put(KEY_PHONE, phone);
            jsonObject.put(KEY_ORGANIZATION, organization);
            jsonObject.put(KEY_DESIGNATION, designation);
            jsonObject.put(KEY_ROLE, VAL_ROLE);
            jsonObject.put(KEY_ROLE_ID, VAL_ROLE_ID);
            jsonObject.put(KEY_DEVICE_CODE, deviceCode);
            jsonObject.put(KEY_DEVICE_TYPE, VAL_DEVICE_TYPE);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject getForgotPasswordRequest(String email) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(KEY_EMAIL, email);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject getEditProfileRequest(String firstName, String lastName,
                                                   String organization, String designation,
                                                   String role, int roleId) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(KEY_FIRST_NAME, firstName);
            jsonObject.put(KEY_LAST_NAME, lastName);
            jsonObject.put(KEY_ORGANIZATION, organization);
            jsonObject.put(KEY_DESIGNATION, designation);
            jsonObject.put(KEY_ROLE, role);
            jsonObject.put(KEY_ROLE_ID, roleId);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject getImageUploadRequest(String profileImg) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(KEY_PROFILE_IMAGE, profileImg);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
