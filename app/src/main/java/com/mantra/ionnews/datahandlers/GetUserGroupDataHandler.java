package com.mantra.ionnews.datahandlers;

import android.content.Context;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mantra.ionnews.interfaces.OnGetUserGroupResponseListener;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.models.responses.UserGroup;
import com.mantra.ionnews.restclient.HttpRequestHandler;
import com.mantra.ionnews.restclient.SuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import static com.mantra.ionnews.restclient.ApiEndpoints.GET_USER_GROUP_REQUEST;
import static com.mantra.ionnews.utils.AppConstants.BASE_URL;

/**
 * Created by rajat on 30/03/17.
 */

public class GetUserGroupDataHandler implements SuccessListener {

    private OnGetUserGroupResponseListener onGetUserGroupResponseListener;
    private Context context;

    public GetUserGroupDataHandler(OnGetUserGroupResponseListener onGetUserGroupResponseListener, Context context) {
        this.onGetUserGroupResponseListener = onGetUserGroupResponseListener;
        this.context = context;
    }

    public void request() {
        HttpRequestHandler.makeJsonArrayRequest(context,
                BASE_URL + GET_USER_GROUP_REQUEST,
                this
        );
    }

    @Override
    public void onSuccess(JSONObject responseObject, JSONArray responseArray, String responseString) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<UserGroup>>() {
        }.getType();
        List<UserGroup> userGroups = gson.fromJson(responseArray.toString(), listType);

        onGetUserGroupResponseListener.getUserGroupResponse(userGroups, null);
    }

    @Override
    public void onError(VolleyError volleyError, String errorMessage, int errorCode) {
        Error error = new Error();
        error.setMessage(errorMessage);
        error.setStatus(errorCode);
        onGetUserGroupResponseListener.getUserGroupResponse(null, error);
    }
}
