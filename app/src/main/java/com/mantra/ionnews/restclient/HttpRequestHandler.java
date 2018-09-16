package com.mantra.ionnews.restclient;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mantra.ionnews.utils.IonNewsApplication;
import com.mantra.ionnews.utils.LocalStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by TaNMay on 3/2/2016.
 */
public class HttpRequestHandler {

    public static String TAG = "HttpRequestHandler ==>";

    public static void makeJsonObjReq(Context context, final JSONObject jObject, String url, int method, final SuccessListener listener) {
        Log.d(TAG, "Make JSON Object Request -");
        Log.d(TAG, "Request Object: " + jObject);
        Log.d(TAG, "Request URL: " + url);
        final String token = LocalStorage.getInstance(context).getToken();
        Log.d(TAG, "Token: " + token);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(method, url, jObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Response: " + response + "");
                listener.onSuccess(response, null, null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                String err, errMsg;
                int errCode = 0;
                errMsg = "Error!";

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    errCode = response.statusCode;
                    if (errCode == 500) {
                        errMsg = "Error #500: Server Error!";
                    } else {
                        json = new String(response.data);
                        errMsg = trimMessage(json, "message");
                    }
                } else if (error instanceof NoConnectionError) {
                    errMsg = "Error: No Internet Connection!";
                }
                VolleyLog.e(TAG, "VolleyError: " + error.getMessage() + error.networkResponse + errMsg);
                listener.onError(error, errMsg, errCode);
            }
        }) {
            public java.util.Map<String, String> getHeaders()
                    throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(15000, 10, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        IonNewsApplication.getInstance().addToRequestQueue(jsonObjReq, TAG);
    }

    public static void makeJsonArrayRequest(Context context, String url, final SuccessListener listener) {
        final String token = LocalStorage.getInstance(context).getToken();
        Log.d(TAG, "Make JSON Array Request -");
        Log.d(TAG, "Request URL: " + url);
        Log.d(TAG, "Token: " + token);

        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "Response: " + response.toString());
                listener.onSuccess(null, response, null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                String err, errMsg;
                int errCode = 0;
                errMsg = "Error!";

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    errCode = response.statusCode;
                    if (errCode == 500) {
                        errMsg = "Error #500: Server Error!";
                    } else {
                        json = new String(response.data);
                        errMsg = trimMessage(json, "message");
                    }
                } else if (error instanceof NoConnectionError) {
                    errMsg = "Error: No Internet Connection!";
                }
                VolleyLog.e(TAG, "VolleyError: " + error.getMessage() + error.networkResponse + errMsg);
                listener.onError(error, errMsg, errCode);
            }
        }) {
            public java.util.Map<String, String> getHeaders()
                    throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", token);
                return headers;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(15000, 10, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        IonNewsApplication.getInstance().addToRequestQueue(req);
    }

    public static String trimMessage(String json, String key) {
        String trimmedString = null;
        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return trimmedString;
    }
}
