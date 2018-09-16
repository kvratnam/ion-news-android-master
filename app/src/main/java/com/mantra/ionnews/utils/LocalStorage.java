package com.mantra.ionnews.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mantra.ionnews.models.User;
import com.mantra.ionnews.models.responses.GetAllLikesResponse;
import com.mantra.ionnews.models.responses.StoriesResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.mantra.ionnews.utils.AppConstants.KEY_SHARED_PREFS_ALL_LIKES;
import static com.mantra.ionnews.utils.AppConstants.KEY_SHARED_PREFS_CHANGE_IN_LIKES;
import static com.mantra.ionnews.utils.AppConstants.KEY_SHARED_PREFS_STORIES;
import static com.mantra.ionnews.utils.AppConstants.KEY_SHARED_PREFS_TOKEN;
import static com.mantra.ionnews.utils.AppConstants.KEY_SHARED_PREFS_USER;

/**
 * Created by TaNMay on 27/09/16.
 */

public class LocalStorage {

    private static LocalStorage instance = null;
    private SharedPreferences sharedPreferences;
    private SharedPreferences secondarySharedPreferences;

    public LocalStorage(Context context) {
        sharedPreferences = context.getSharedPreferences("Reg", 0);
        secondarySharedPreferences = context.getSharedPreferences("Device", 0);
    }

    public static LocalStorage getInstance(Context context) {
        if (instance == null) {
            synchronized (LocalStorage.class) {
                if (instance == null) {
                    instance = new LocalStorage(context);
                }
            }
        }
        return instance;
    }

    public void clearLocalStorage() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public String getToken() {
        if (sharedPreferences.contains(KEY_SHARED_PREFS_TOKEN))
            return sharedPreferences.getString(KEY_SHARED_PREFS_TOKEN, null);
        else return null;
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SHARED_PREFS_TOKEN, token);
        editor.commit();
    }

    public User getUser() {
        if (sharedPreferences.contains(KEY_SHARED_PREFS_USER)) {
            User user = new Gson().fromJson(sharedPreferences.getString(KEY_SHARED_PREFS_USER, null), User.class);
            return user;
        } else return null;
    }

    public void setUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SHARED_PREFS_USER, new Gson().toJson(user));
        editor.commit();
    }

    public List<StoriesResponse> getStories() {
        if (sharedPreferences.contains(KEY_SHARED_PREFS_STORIES)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<StoriesResponse>>() {
            }.getType();
            List<StoriesResponse> stories = gson.fromJson(
                    sharedPreferences.getString(KEY_SHARED_PREFS_STORIES, null), listType);
            return stories;
        } else return new ArrayList<>();
    }

    public void setStories(String stories) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SHARED_PREFS_STORIES, stories);
        editor.commit();
    }

    public boolean isAnyChangeInLike() {
        if (sharedPreferences.contains(KEY_SHARED_PREFS_CHANGE_IN_LIKES)) {
            return sharedPreferences.getBoolean(KEY_SHARED_PREFS_CHANGE_IN_LIKES, false);
        } else return false;
    }

    public void setChangeInLike(boolean changeInLike) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_SHARED_PREFS_CHANGE_IN_LIKES, changeInLike);
        editor.commit();
    }

    public GetAllLikesResponse getAllLikes() {
        if (sharedPreferences.contains(KEY_SHARED_PREFS_ALL_LIKES)) {
            GetAllLikesResponse getAllLikesResponse = new Gson().fromJson(sharedPreferences.getString(KEY_SHARED_PREFS_ALL_LIKES, null), GetAllLikesResponse.class);
            return getAllLikesResponse;
        } else return null;
    }

    public void setAllLikes(GetAllLikesResponse getAllLikesResponse) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SHARED_PREFS_ALL_LIKES, new Gson().toJson(getAllLikesResponse));
        editor.commit();
    }
}