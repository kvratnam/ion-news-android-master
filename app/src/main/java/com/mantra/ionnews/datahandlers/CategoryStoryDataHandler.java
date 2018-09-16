package com.mantra.ionnews.datahandlers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.mantra.ionnews.interfaces.BaseResponseInterface;
import com.mantra.ionnews.models.responses.CategoryStoryResponse;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.restclient.HttpRequestHandler;
import com.mantra.ionnews.restclient.SuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.mantra.ionnews.restclient.ApiEndpoints.CATEGORY_FETCH_NEWS;
import static com.mantra.ionnews.restclient.ApiEndpoints.CATEGORY_FETCH_NEWS_PAGE;
import static com.mantra.ionnews.utils.AppConstants.BASE_URL;

/**
 * Created by rajat on 30/03/17.
 */

public class CategoryStoryDataHandler implements SuccessListener {

    private BaseResponseInterface baseResponseInterface;
    private Context context;

    public CategoryStoryDataHandler(BaseResponseInterface baseResponseInterface, Context context) {
        this.baseResponseInterface = baseResponseInterface;
        this.context = context;
    }

    public void request(String id, int pageNum) {
        if (pageNum == -1) {
            HttpRequestHandler.makeJsonObjReq(context,
                    null,
                    BASE_URL + CATEGORY_FETCH_NEWS + id,
                    Request.Method.GET,
                    this
            );
        } else {
            HttpRequestHandler.makeJsonObjReq(context,
                    null,
                    BASE_URL + CATEGORY_FETCH_NEWS + id + CATEGORY_FETCH_NEWS_PAGE + pageNum,
                    Request.Method.GET,
                    this
            );
        }
    }

    @Override
    public void onSuccess(JSONObject responseObject, JSONArray responseArray, String responseString) {
        Gson gson = new Gson();
        CategoryStoryResponse categoryStoryResponse = gson.fromJson(responseObject.toString(), CategoryStoryResponse.class);
        baseResponseInterface.response(categoryStoryResponse, null);
    }

    @Override
    public void onError(VolleyError volleyError, String errorMessage, int errorCode) {
        Error error = new Error();
        error.setMessage(errorMessage);
        error.setStatus(errorCode);
        baseResponseInterface.response(null, error);
    }
}
