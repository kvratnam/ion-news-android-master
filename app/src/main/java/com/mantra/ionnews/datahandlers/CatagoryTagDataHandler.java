package com.mantra.ionnews.datahandlers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mantra.ionnews.interfaces.BaseResponseInterface;
import com.mantra.ionnews.models.Story;
import com.mantra.ionnews.models.responses.CatagoryTagResponse;
import com.mantra.ionnews.models.responses.CategoryStoryResponse;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.models.responses.SearchTagResponse;
import com.mantra.ionnews.models.responses.StoriesResponse;
import com.mantra.ionnews.restclient.HttpRequestHandler;
import com.mantra.ionnews.restclient.SuccessListener;
import com.mantra.ionnews.utils.LocalStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.mantra.ionnews.restclient.ApiEndpoints.CATAGORY_AND_TAG_LIST;
import static com.mantra.ionnews.utils.AppConstants.BASE_URL;
import static com.mantra.ionnews.utils.IonNewsApplication.TAG;

public class CatagoryTagDataHandler implements SuccessListener {

    private BaseResponseInterface baseResponseInterface;
    private Context context;

    public CatagoryTagDataHandler(BaseResponseInterface baseResponseInterface, Context context) {
        this.baseResponseInterface = baseResponseInterface;
        this.context = context;
    }

    public void request() {
        HttpRequestHandler.makeJsonObjReq(context,
                null,
                BASE_URL + CATAGORY_AND_TAG_LIST,
                Request.Method.GET,
                this
        );
    }

    @Override
    public void onSuccess(JSONObject responseObject, JSONArray responseArray, String responseString) {

        Gson gson = new Gson();
        SearchTagResponse searchTagResponse = gson.fromJson(responseObject.toString(), SearchTagResponse.class);
        baseResponseInterface.response(searchTagResponse, null);

        try {
            // jsonString is a string variable that holds the JSON
            List<String> list = new ArrayList<String>();
            JSONObject jsonObject= new JSONObject(responseObject.toString());
            JSONArray itemArray=jsonObject.optJSONArray("tag");

            for (int i = 0; i < itemArray.length(); i++) {
                String value=itemArray.getString(i);
                list.add(value);
                Log.d(TAG, "onSuccess: "+list);
            }


            JSONArray catagoryArray=jsonObject.optJSONArray("category");

            for (int i = 0; i < catagoryArray.length(); i++) {
                String id = catagoryArray.optJSONObject(i).optString("id");
                String name = catagoryArray.optJSONObject(i).optString("name");
                String slugName = catagoryArray.optJSONObject(i).optString("slug_name");
                Log.d("TAG", "id "+ id + " ask "+ name+"slugName"+slugName);

            }




        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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