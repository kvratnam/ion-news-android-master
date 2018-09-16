package com.mantra.ionnews.restclient;

/**
 * Created by TaNMay on 21/03/17.
 */

public class ApiEndpoints {

    public static final String SIGN_IN_REQUEST = "authentication/login";
    public static final String SIGN_UP_REQUEST = "authentication/register";

    public static final String GET_USER_GROUP_REQUEST = "category/user_group";
    public static final String FORGOT_PASSWORD_REQUEST = "authentication/forgot";
    public static final String EDIT_PROFILE_REQUEST = "authentication/update";
    public static final String IMAGE_UPLOAD_REQUEST = "authentication/updateProfileImg";

    public static final String FETCH_HOME_PAGE = "story/homepage";
    public static final String CATEGORY_FETCH_NEWS = "story/list?category_id=";
    public static final String CATEGORY_FETCH_NEWS_PAGE = "&page=";

    public static final String FETCH_ALL_LIKES = "story/getAllLikeStory";
    public static final String FETCH_ALL_LIKES_PAGE = "?page=";
    public static final String LIKE_STORY_REQUEST = "story/story_like";
}
