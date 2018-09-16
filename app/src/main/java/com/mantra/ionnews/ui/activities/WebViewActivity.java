package com.mantra.ionnews.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.mantra.ionnews.R;
import com.mantra.ionnews.datahandlers.LikeStoryDataHandler;
import com.mantra.ionnews.interfaces.OnLikeStoryResponseListener;
import com.mantra.ionnews.models.Story;
import com.mantra.ionnews.models.requests.LikeStoryRequest;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.utils.AppConstants;
import com.mantra.ionnews.utils.CustomShareIntent;
import com.mantra.ionnews.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import static com.mantra.ionnews.utils.AppConstants.KEY_PUSH_FROM_NOTIFICATION;
import static com.mantra.ionnews.utils.AppConstants.KEY_PUSH_STORY_ID;
import static com.mantra.ionnews.utils.AppConstants.KEY_STORY_ITEM;
import static com.mantra.ionnews.utils.AppConstants.VAL_ADS;
import static com.mantra.ionnews.utils.AppConstants.VAL_NEWS;
import static com.mantra.ionnews.utils.ConstantClass.WEB_VIEW;

public class WebViewActivity extends BaseActivity implements OnLikeStoryResponseListener {

    public static OnLikeUpdatedListener onLikeUpdatedListener;

    private String TAG = WEB_VIEW;

    private Toolbar toolbar;
    private RelativeLayout mainView;
    private WebView mWebView;
    private ImageView like, share;
    private ProgressBar progressBar;

    private boolean isFromNotification = false;
    private String crawlUrl, storyImage = null;
    private Story story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        initView();

        CookieManager.getInstance().setAcceptCookie(true);

        isFromNotification = getIntent().getBooleanExtra(KEY_PUSH_FROM_NOTIFICATION, false);
        if (isFromNotification) {
            crawlUrl = getIntent().getStringExtra(KEY_PUSH_STORY_ID);
        } else {

            story = (Story) getIntent().getExtras().getSerializable(KEY_STORY_ITEM);
            Uri intentUri = getIntent().getData();
            if (story != null) {
                if (story.getType().equals(AppConstants.VAL_ADS)) crawlUrl = story.getCustomUrl();
                else crawlUrl = story.getCrawlUrl();
                storyImage = story.getImage();
            } else crawlUrl = intentUri.toString();

        }

        setUpToolbar();
        initWebView();
    }

    private void initWebView() {
        if (Util.hasInternetAccess(this))
            setUpWebView(crawlUrl);
        else showNetworkErrorMessage(
                mainView,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initWebView();
                    }
                },
                this
        );

    }

    private void setUpWebView(String crawlUrl) {

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource(WebView view, String url) {

            }

            public void onPageFinished(WebView view, String url) {
                hideProgressBar(progressBar);
            }

        });

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(crawlUrl);
        CookieManager.getInstance().setAcceptCookie(true);
        showProgressBar(progressBar);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (story != null) {
            if (story.getType() != null && story.getType().equalsIgnoreCase(VAL_ADS)) {
                like.setVisibility(View.INVISIBLE);
                share.setVisibility(View.INVISIBLE);
            } else {
                updateStoryLikeStatus();

                if (story.getIslike() != null) {
                    like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (story.getIslike() == 1) {
                                like.setImageDrawable(ContextCompat.getDrawable(WebViewActivity.this, R.drawable.ic_unlike));
                                story.setIslike(0);
                            } else {
                                like.setImageDrawable(ContextCompat.getDrawable(WebViewActivity.this, R.drawable.ic_like));
                                story.setIslike(1);
                            }

                            LikeStoryRequest likeStoryRequest = new LikeStoryRequest();
                            likeStoryRequest.setContentId(story.getId());
                            initLikeStoryRequest(likeStoryRequest, -1);
                        }
                    });
                }

                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new CustomShareIntent(
                                WebViewActivity.this,
                                storyImage,
                                crawlUrl,
                                "Check out this story: " + crawlUrl + "from ION News!"
                        ).share();
                    }
                });
            }
        } else {
            if (isFromNotification && crawlUrl != null) {
                like.setVisibility(View.GONE);
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new CustomShareIntent(
                                WebViewActivity.this,
                                storyImage,
                                crawlUrl,
                                "Check out this story: " + crawlUrl + "from ION News!"
                        ).share();
                    }
                });
            }
        }
    }

    private void updateStoryLikeStatus() {
        if (story.getIslike() != null) {
            if (story.getIslike() == 1) {
                like.setImageDrawable(ContextCompat.getDrawable(WebViewActivity.this, R.drawable.ic_like));
            } else {
                like.setImageDrawable(ContextCompat.getDrawable(WebViewActivity.this, R.drawable.ic_unlike));
            }
        }
    }

    private void initLikeStoryRequest(final LikeStoryRequest likeStoryRequest, final int position) {
        if (Util.hasInternetAccess(this)) {
            likeStory(likeStoryRequest, position);
        } else {
            showNetworkErrorMessage(
                    mainView,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            likeStory(likeStoryRequest, position);
                        }
                    },
                    this
            );
        }
    }

    private void likeStory(LikeStoryRequest likeStoryRequest, int position) {
        Gson gson = new Gson();
        try {
            JSONObject likeStoryJson = new JSONObject(gson.toJson(likeStoryRequest));
            LikeStoryDataHandler likeStoryDataHandler = new LikeStoryDataHandler(this, this);
            likeStoryDataHandler.request(likeStoryJson, position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mWebView = (WebView) findViewById(R.id.cwv_webview);
        like = (ImageView) findViewById(R.id.awv_like);
        share = (ImageView) findViewById(R.id.awv_share);
        progressBar = (ProgressBar) findViewById(R.id.cwv_progress);
        mainView = (RelativeLayout) findViewById(R.id.cwv_main_view);
    }

    @Override
    public void likeStoryResponse(String message, Error error, int index) {
        if (error == null) {

        } else {
            if (story.getIslike() == 1)
                story.setIslike(0);
            else story.setIslike(1);
            updateStoryLikeStatus();
        }
    }

    @Override
    protected void onDestroy() {
        if (story != null && story.getType() != null && story.getType().equalsIgnoreCase(VAL_NEWS) && story.getIslike() != null) {
            if (story.getIslike() == 1) onLikeUpdatedListener.onLikeUpdated(true);
            else onLikeUpdatedListener.onLikeUpdated(false);
        } else if (isFromNotification) {
            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
        }
        super.onDestroy();
    }

    public interface OnLikeUpdatedListener {

        void onLikeUpdated(boolean isLiked);
    }
}
