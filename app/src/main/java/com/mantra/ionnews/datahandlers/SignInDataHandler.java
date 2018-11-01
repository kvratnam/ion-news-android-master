package com.mantra.ionnews.datahandlers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.mantra.ionnews.interfaces.BaseResponseInterface;
import com.mantra.ionnews.models.User;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.models.responses.SignInResponse;
import com.mantra.ionnews.restclient.HttpRequestHandler;
import com.mantra.ionnews.restclient.SuccessListener;
import com.mantra.ionnews.utils.LocalStorage;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.mantra.ionnews.restclient.ApiEndpoints.SIGN_IN_REQUEST;
import static com.mantra.ionnews.utils.AppConstants.BASE_URL;

/**
 * Created by TaNMay on 19/02/17.
 */
public class SignInDataHandler implements SuccessListener {

    private BaseResponseInterface baseResponseInterface;
    private Context context;

    public SignInDataHandler(BaseResponseInterface baseResponseInterface, Context context) {
        this.baseResponseInterface = baseResponseInterface;
        this.context = context;
    }

    public void request(JSONObject request) {

        HttpRequestHandler.makeJsonObjReq(context,
                request,
                BASE_URL + SIGN_IN_REQUEST,
                Request.Method.POST,
                this
        );
    }

    @Override
    public void onSuccess(JSONObject responseObject, JSONArray responseArray, String responseString) {
        LocalStorage localStorage = LocalStorage.getInstance(context);
        SignInResponse signInResponse = new Gson().fromJson(responseObject.toString(), SignInResponse.class);
        User user = new Gson().fromJson(responseObject.toString(), User.class);
        localStorage.setUser(user);
        localStorage.setToken(signInResponse.getToken());
        localStorage.setUserID(signInResponse.getRoleID());
        localStorage.setID(String.valueOf(signInResponse.getId()));
        baseResponseInterface.response(signInResponse, null);
    }

    @Override
    public void onError(VolleyError volleyError, String errorMessage, int errorCode) {
        Error error = new Error();
        error.setMessage(errorMessage);
        error.setStatus(errorCode);
        baseResponseInterface.response(null, error);
    }
}
