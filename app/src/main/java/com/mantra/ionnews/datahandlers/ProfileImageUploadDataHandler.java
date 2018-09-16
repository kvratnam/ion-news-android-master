package com.mantra.ionnews.datahandlers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.mantra.ionnews.interfaces.OnImageUploadResponseListener;
import com.mantra.ionnews.models.User;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.models.responses.ImageUploadResponse;
import com.mantra.ionnews.restclient.HttpRequestHandler;
import com.mantra.ionnews.restclient.SuccessListener;
import com.mantra.ionnews.utils.LocalStorage;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.mantra.ionnews.restclient.ApiEndpoints.IMAGE_UPLOAD_REQUEST;
import static com.mantra.ionnews.utils.AppConstants.BASE_URL;

/**
 * Created by TaNMay on 19/02/17.
 */
public class ProfileImageUploadDataHandler implements SuccessListener {

    private OnImageUploadResponseListener onImageUploadResponseListener;
    private Context context;

    public ProfileImageUploadDataHandler(OnImageUploadResponseListener onImageUploadResponseListener, Context context) {
        this.onImageUploadResponseListener = onImageUploadResponseListener;
        this.context = context;
    }

    public void request(JSONObject request) {

        HttpRequestHandler.makeJsonObjReq(context,
                request,
                BASE_URL + IMAGE_UPLOAD_REQUEST,
                Request.Method.POST,
                this
        );
    }

    @Override
    public void onSuccess(JSONObject responseObject, JSONArray responseArray, String responseString) {
        LocalStorage localStorage = LocalStorage.getInstance(context);
        ImageUploadResponse imageUploadResponse = new Gson().fromJson(responseObject.toString(), ImageUploadResponse.class);
        User user = localStorage.getUser();
        user.setProfileImg(imageUploadResponse.getProfileImg());
        localStorage.setUser(user);
        onImageUploadResponseListener.imageUploadResponse(imageUploadResponse, null);
    }

    @Override
    public void onError(VolleyError volleyError, String errorMessage, int errorCode) {
        Error error = new Error();
        error.setMessage(errorMessage);
        error.setStatus(errorCode);
        onImageUploadResponseListener.imageUploadResponse(null, error);
    }
}
