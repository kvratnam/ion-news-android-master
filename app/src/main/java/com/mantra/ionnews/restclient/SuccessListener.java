package com.mantra.ionnews.restclient;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by TaNMay on 3/2/2016.
 */

public interface SuccessListener {

    void onSuccess(JSONObject responseObject, JSONArray responseArray, String responseString);

    void onError(VolleyError volleyError, String errorMessage, int errorCode);

}
