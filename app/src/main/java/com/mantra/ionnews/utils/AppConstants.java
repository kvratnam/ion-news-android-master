package com.mantra.ionnews.utils;

import static com.mantra.ionnews.utils.ConstantClass.IS_DEV;

/**
 * Created by TaNMay on 15/02/17.
 */

public class AppConstants {

    public static final String APP_NAME = "Ion News";

    public static final String BASE_DEV_URL = "http://34.211.114.131/api/";
    public static final String BASE_PROD_URL = "http://52.89.47.47/api/";

    public static final String BASE_URL = IS_DEV ? BASE_DEV_URL : BASE_PROD_URL;

    public static final String KEY_SHARED_PREFS_TOKEN = "TOKEN";
    public static final String KEY_SHARED_PREFS_DEVICE_ID = "DEVICE_ID";
    public static final String KEY_SHARED_PREFS_USER = "USER";
    public static final String KEY_SHARED_PREFS_CHANGE_IN_LIKES = "CHANGE_IN_LIKES";
    public static final String KEY_SHARED_PREFS_STORIES = "STORIES";
    public static final String KEY_SHARED_PREFS_ALL_LIKES = "ALL_LIKES";

    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_ORGANIZATION = "company";
    public static final String KEY_DESIGNATION = "designation";
    public static final String KEY_ROLE = "role";
    public static final String KEY_ROLE_ID = "role_id";
    public static final String KEY_DEVICE_CODE = "device_code";
    public static final String KEY_PROFILE_IMAGE = "profileImg";
    public static final String KEY_DEVICE_TYPE = "device_type";

    public static final String KEY_CATEGORY_STORIES = "category_stories";
    public static final String KEY_CATEGORY_NAME = "category_name";
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_CRAWL_URL = "crawl_url";
    public static final String KEY_STORY_ITEM = "story_item";
    public static final String KEY_LIKED_STORIES = "liked_stories";
    public static final String KEY_LIKED_STORY_INDEX = "liked_story_index";

    public static final String KEY_MESSAGE = "message";

    public static final String VAL_DEVICE_TYPE = "android";

    public static final String VAL_ROLE = "user";
    public static final String VAL_ROLE_ID = "2";
    public static final String VAL_NEWS = "news";
    public static final String VAL_ADS = "ad";

    public static final String VAL_FOOTER_MENU_LIKE = "Like";
    public static final String VAL_FOOTER_MENU_LIKED = "Liked";

    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String PUSH_NOTIFICATION = "PUSH_NOTIFICATION";
    public static final String REGISTRATION_COMPLETE = "REGISTRATION_COMPLETE";

    public static final String KEY_PUSH_MESSAGE = "message";
    public static final String KEY_PUSH_FROM_NOTIFICATION = "from_notification";
    public static final String KEY_PUSH_STORY_ID = "storyId";
}


