package com.mantra.ionnews.datahandlers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mantra.ionnews.interfaces.BaseResponseInterface;
import com.mantra.ionnews.models.Story;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.models.responses.StoriesResponse;
import com.mantra.ionnews.restclient.HttpRequestHandler;
import com.mantra.ionnews.restclient.SuccessListener;
import com.mantra.ionnews.utils.LocalStorage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.mantra.ionnews.restclient.ApiEndpoints.FETCH_HOME_PAGE;
import static com.mantra.ionnews.utils.AppConstants.BASE_URL;

/**
 * Created by rajat on 30/03/17.
 */

public class StoriesDataHandler implements SuccessListener {

    private BaseResponseInterface baseResponseInterface;
    private Context context;

    public StoriesDataHandler(BaseResponseInterface baseResponseInterface, Context context) {
        this.baseResponseInterface = baseResponseInterface;
        this.context = context;
    }

    public void request() {
        HttpRequestHandler.makeJsonObjReq(context,
                null,
                BASE_URL + FETCH_HOME_PAGE,
                Request.Method.GET,
                this
        );
    }

    @Override
    public void onSuccess(JSONObject responseObject, JSONArray responseArray, String responseString) {
        Gson gson = new Gson();
        List<StoriesResponse> storiesResponseList = new ArrayList<>();
        Iterator<?> keys = responseObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            StoriesResponse storiesResponse = new StoriesResponse();
            storiesResponse.setCategoryTitle(key);
            if (responseObject.optJSONArray(key) != null && responseObject.optJSONArray(key).length() > 0) {
                Type storyListType = new TypeToken<List<Story>>() {
                }.getType();
                List<Story> stories = gson.fromJson(responseObject.optJSONArray(key).toString(), storyListType);
                storiesResponse.setCategoryStories(stories);
                storiesResponseList.add(storiesResponse);
            }
        }

        LocalStorage.getInstance(context).setStories(gson.toJson(storiesResponseList));

        baseResponseInterface.response(storiesResponseList, null);
    }

    @Override
    public void onError(VolleyError volleyError, String errorMessage, int errorCode) {
        Error error = new Error();
        error.setMessage(errorMessage);
        error.setStatus(errorCode);
        baseResponseInterface.response(null, error);
    }
}
