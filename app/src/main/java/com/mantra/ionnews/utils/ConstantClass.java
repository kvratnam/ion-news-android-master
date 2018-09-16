package com.mantra.ionnews.utils;


import com.mantra.ionnews.R;

/**
 * Created by TaNMay on 28/09/16.
 */
public class ConstantClass {

    public static final int SPLASH_TIME_OUT = 2000;
    public static final int DUMMY_API_CALL_TIME_OUT = 3000;
    // Activities
    public static final String BASE = "Base ==>";
    public static final String ONBOARDING = "Onboarding ==>";
    public static final String DASHBOARD = "Dashboard ==>";
    public static final String NEWS_DETAIL = "NewsDetail ==>";
    public static final String WEB_VIEW = "WebView ==>";
    // Fragments
    public static final String PROFILE = "PROFILE";
    public static final String NEWS = "NEWS";
    public static final String STORIES = "STORIES";
    public static final String SETTINGS = "SETTINGS";
    public static final String EDIT_PROFILE = "EDIT_PROFILE";
    // BottomSheets
    public static final String SIGN_IN = "SIGN_IN";
    public static final String SIGN_UP = "SIGN_UP";
    public static final String NEWS_DETAIL_FOOTER_MENU = "NEWS_DETAIL_FOOTER_MENU";
    // Services
    public static final String FCM_SERVICE = "FCM ==>";
    // Fragment Click (Event Bus)
    public static final String ON_GOTO_NEWS_FRAGMENT = "ON_GOTO_NEWS_FRAGMENT";
    public static final String ON_GOTO_PROFILE_FRAGMENT = "ON_GOTO_PROFILE_FRAGMENT";
    public static final String WITH_STORIES = "WITH_STORIES";
    public static final String WITH_LIKES = "WITH_LIKES";
    // Dashboard Request (Event Bus)
    public static final String STORIES_RESPONSE = "STORIES_RESPONSE";
    // News Detail
    public static final int MAX_LINES_NEWS_DESC = 4;
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    // News Detail Menu
    public static final String[] NEWS_FOOTER_MENU_OPTIONS = {
            "Like",
            "Share",
            "View on Web"
    };
    public static final int[] NEWS_FOOTER_MENU_OPTION_ICONS = {
            R.drawable.ic_unlike,
            R.drawable.ic_share,
            R.drawable.ic_view_on_web
    };
    public static final String[] ADS_FOOTER_MENU_OPTIONS = {
            "View on Web"
    };
    public static final int[] ADS_FOOTER_MENU_OPTION_ICONS = {
            R.drawable.ic_view_on_web
    };
    public static boolean IS_DEV = false;
}
