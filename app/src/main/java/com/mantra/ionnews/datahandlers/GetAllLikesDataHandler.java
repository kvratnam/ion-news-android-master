package com.mantra.ionnews.datahandlers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.mantra.ionnews.interfaces.OnGetAllLikesResponseListener;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.models.responses.GetAllLikesResponse;
import com.mantra.ionnews.restclient.HttpRequestHandler;
import com.mantra.ionnews.restclient.SuccessListener;
import com.mantra.ionnews.utils.LocalStorage;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.mantra.ionnews.restclient.ApiEndpoints.FETCH_ALL_LIKES;
import static com.mantra.ionnews.restclient.ApiEndpoints.FETCH_ALL_LIKES_PAGE;
import static com.mantra.ionnews.utils.AppConstants.BASE_URL;

/**
 * Created by rajat on 30/03/17.
 */

public class GetAllLikesDataHandler implements SuccessListener {

    private OnGetAllLikesResponseListener onGetAllLikesResponseListener;
    private Context context;
    private boolean isFresh;

    public GetAllLikesDataHandler(OnGetAllLikesResponseListener onGetAllLikesResponseListener, Context context) {
        this.onGetAllLikesResponseListener = onGetAllLikesResponseListener;
        this.context = context;
    }

    public void request(int pageNum, boolean isFresh) {
        this.isFresh = isFresh;
        if (pageNum == -1) {
            HttpRequestHandler.makeJsonObjReq(context,
                    null,
                    BASE_URL + FETCH_ALL_LIKES,
                    Request.Method.GET,
                    this
            );
        } else {
            HttpRequestHandler.makeJsonObjReq(context,
                    null,
                    BASE_URL + FETCH_ALL_LIKES + FETCH_ALL_LIKES_PAGE + pageNum,
                    Request.Method.GET,
                    this
            );
        }
    }

    @Override
    public void onSuccess(JSONObject responseObject, JSONArray responseArray, String responseString) {
        Gson gson = new Gson();
        GetAllLikesResponse getAllLikesResponse = gson.fromJson(responseObject.toString(), GetAllLikesResponse.class);
        LocalStorage.getInstance(context).setChangeInLike(false);

        if (isFresh) {
            LocalStorage.getInstance(context).setAllLikes(getAllLikesResponse);
        }

        onGetAllLikesResponseListener.getAllLikesResponse(getAllLikesResponse, null, isFresh);
    }

    @Override
    public void onError(VolleyError volleyError, String errorMessage, int errorCode) {
        Error error = new Error();
        error.setMessage(errorMessage);
        error.setStatus(errorCode);
        onGetAllLikesResponseListener.getAllLikesResponse(null, error, isFresh);
    }
}
