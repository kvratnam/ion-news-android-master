package com.mantra.ionnews.datahandlers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.mantra.ionnews.interfaces.BaseResponseInterface;
import com.mantra.ionnews.models.User;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.restclient.HttpRequestHandler;
import com.mantra.ionnews.restclient.SuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.mantra.ionnews.restclient.ApiEndpoints.EDIT_PROFILE_REQUEST;
import static com.mantra.ionnews.utils.AppConstants.BASE_URL;

/**
 * Created by rajat on 30/03/17.
 */

public class EditProfileDataHandler implements SuccessListener {

    private BaseResponseInterface baseResponseInterface;
    private Context context;

    public EditProfileDataHandler(BaseResponseInterface baseResponseInterface, Context context) {
        this.baseResponseInterface = baseResponseInterface;
        this.context = context;
    }

    public void request(JSONObject request) {
        HttpRequestHandler.makeJsonObjReq(context,
                request,
                BASE_URL + EDIT_PROFILE_REQUEST,
                Request.Method.POST,
                this
        );
    }

    @Override
    public void onSuccess(JSONObject responseObject, JSONArray responseArray, String responseString) {
        Gson gson = new Gson();
        User editProfileResponse = gson.fromJson(responseObject.toString(), User.class);
        baseResponseInterface.response(editProfileResponse, null);
    }

    @Override
    public void onError(VolleyError volleyError, String errorMessage, int errorCode) {
        Error error = new Error();
        error.setMessage(errorMessage);
        error.setStatus(errorCode);
        baseResponseInterface.response(null, error);
    }
}
