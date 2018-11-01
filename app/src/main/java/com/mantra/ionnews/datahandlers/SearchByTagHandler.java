package com.mantra.ionnews.datahandlers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.mantra.ionnews.interfaces.BaseResponseInterface;
import com.mantra.ionnews.models.User;
import com.mantra.ionnews.models.responses.CategoryStoryResponse;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.models.responses.SearchTagResponse;
import com.mantra.ionnews.models.responses.SignInResponse;
import com.mantra.ionnews.restclient.HttpRequestHandler;
import com.mantra.ionnews.restclient.SuccessListener;
import com.mantra.ionnews.utils.LocalStorage;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.mantra.ionnews.restclient.ApiEndpoints.SEARCH_STORY_BY_TAG;
import static com.mantra.ionnews.utils.AppConstants.BASE_URL;

public class SearchByTagHandler implements SuccessListener {

    private BaseResponseInterface baseResponseInterface;
    private Context context;

    public SearchByTagHandler(BaseResponseInterface baseResponseInterface, Context context) {
        this.baseResponseInterface = baseResponseInterface;
        this.context = context;
    }

    public void request(JSONObject request) {

        HttpRequestHandler.makeJsonObjReq(context,
                request,
                BASE_URL + SEARCH_STORY_BY_TAG,
                Request.Method.POST,
                this
        );
    }

    @Override
    public void onSuccess(JSONObject responseObject, JSONArray responseArray, String responseString) {
        Gson gson = new Gson();
        SearchTagResponse searchTagResponse = gson.fromJson(responseObject.toString(), SearchTagResponse.class);
        baseResponseInterface.response(searchTagResponse, null);
    }

    @Override
    public void onError(VolleyError volleyError, String errorMessage, int errorCode) {
        Error error = new Error();
        error.setMessage(errorMessage);
        error.setStatus(errorCode);
        baseResponseInterface.response(null, error);
    }
}