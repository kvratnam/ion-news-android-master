package com.mantra.ionnews.datahandlers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.mantra.ionnews.interfaces.OnLikeStoryResponseListener;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.restclient.HttpRequestHandler;
import com.mantra.ionnews.restclient.SuccessListener;
import com.mantra.ionnews.utils.AppConstants;
import com.mantra.ionnews.utils.LocalStorage;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.mantra.ionnews.restclient.ApiEndpoints.LIKE_STORY_REQUEST;
import static com.mantra.ionnews.utils.AppConstants.BASE_URL;

/**
 * Created by TaNMay on 19/02/17.
 */
public class LikeStoryDataHandler implements SuccessListener {

    private OnLikeStoryResponseListener onLikeStoryResponseListener;
    private Context context;
    private int index;

    public LikeStoryDataHandler(OnLikeStoryResponseListener onLikeStoryResponseListener, Context context) {
        this.onLikeStoryResponseListener = onLikeStoryResponseListener;
        this.context = context;
    }

    public void request(JSONObject request, int position) {
        index = position;
        HttpRequestHandler.makeJsonObjReq(context,
                request,
                BASE_URL + LIKE_STORY_REQUEST,
                Request.Method.POST,
                this
        );
    }

    @Override
    public void onSuccess(JSONObject responseObject, JSONArray responseArray, String responseString) {
        String message = responseObject.optString(AppConstants.KEY_MESSAGE);
        LocalStorage.getInstance(context).setChangeInLike(true);
        onLikeStoryResponseListener.likeStoryResponse(message, null, index);
    }

    @Override
    public void onError(VolleyError volleyError, String errorMessage, int errorCode) {
        Error error = new Error();
        error.setMessage(errorMessage);
        error.setStatus(errorCode);
        onLikeStoryResponseListener.likeStoryResponse(null, error, index);
    }
}
