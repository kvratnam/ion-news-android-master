package com.mantra.ionnews.datahandlers;

import android.content.Context;
import android.widget.Toast;

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

import static com.mantra.ionnews.restclient.ApiEndpoints.TAG_NAME;
import static com.mantra.ionnews.restclient.ApiEndpoints.TAG_STORY_BY_TAG;
import static com.mantra.ionnews.utils.AppConstants.BASE_URL;

/**
 * Created by rajat on 30/03/17.
 */

public class TagDataHandler implements SuccessListener {

    private BaseResponseInterface baseResponseInterface;
    private Context context;

    public TagDataHandler(BaseResponseInterface baseResponseInterface, Context context) {
        this.baseResponseInterface = baseResponseInterface;
        this.context = context;
    }

    public void request(String id,String tagName) {

        HttpRequestHandler.makeJsonObjReq(context,
                null,
                BASE_URL + TAG_STORY_BY_TAG +id+TAG_NAME + tagName,
                Request.Method.POST,
                this
        );
    }

    @Override
    public void onSuccess(JSONObject responseObject, JSONArray responseArray, String responseString) {

        JSONObject jsonObject= responseObject.optJSONObject("data").optJSONObject("all_data");
        if(jsonObject!= null) {
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
        else {
            Toast.makeText(context,"No Result Found",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(VolleyError volleyError, String errorMessage, int errorCode) {
        Error error = new Error();
        error.setMessage(errorMessage);
        error.setStatus(errorCode);
        baseResponseInterface.response(null, error);
    }
}
