package com.mantra.ionnews.datahandlers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.mantra.ionnews.interfaces.OnForgotPasswordResponseListener;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.restclient.HttpRequestHandler;
import com.mantra.ionnews.restclient.SuccessListener;
import com.mantra.ionnews.utils.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.mantra.ionnews.restclient.ApiEndpoints.FORGOT_PASSWORD_REQUEST;
import static com.mantra.ionnews.utils.AppConstants.BASE_URL;

/**
 * Created by TaNMay on 19/02/17.
 */
public class ForgotPasswordDataHandler implements SuccessListener {

    private OnForgotPasswordResponseListener onForgotPasswordResponseListener;
    private Context context;

    public ForgotPasswordDataHandler(OnForgotPasswordResponseListener onForgotPasswordResponseListener, Context context) {
        this.onForgotPasswordResponseListener = onForgotPasswordResponseListener;
        this.context = context;
    }

    public void request(JSONObject request) {
        HttpRequestHandler.makeJsonObjReq(context,
                request,
                BASE_URL + FORGOT_PASSWORD_REQUEST,
                Request.Method.POST,
                this
        );
    }

    @Override
    public void onSuccess(JSONObject responseObject, JSONArray responseArray, String responseString) {
        String message = responseObject.optString(AppConstants.KEY_MESSAGE);
        onForgotPasswordResponseListener.forgotPasswordResponse(message, null);
    }

    @Override
    public void onError(VolleyError volleyError, String errorMessage, int errorCode) {
        Error error = new Error();
        error.setMessage(errorMessage);
        error.setStatus(errorCode);
        onForgotPasswordResponseListener.forgotPasswordResponse(null, error);
    }
}
