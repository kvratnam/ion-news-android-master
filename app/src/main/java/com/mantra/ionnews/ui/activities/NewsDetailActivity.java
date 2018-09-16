package com.mantra.ionnews.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.mantra.ionnews.R;
import com.mantra.ionnews.adapters.FlipAdapter;
import com.mantra.ionnews.datahandlers.CategoryStoryDataHandler;
import com.mantra.ionnews.datahandlers.LikeStoryDataHandler;
import com.mantra.ionnews.interfaces.BaseResponseInterface;
import com.mantra.ionnews.interfaces.OnLikeStoryResponseListener;
import com.mantra.ionnews.interfaces.OnNewsFlipItemClickListener;
import com.mantra.ionnews.interfaces.OnPopupDialogClickListener;
import com.mantra.ionnews.models.Story;
import com.mantra.ionnews.models.requests.LikeStoryRequest;
import com.mantra.ionnews.models.responses.CategoryStoryResponse;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.models.responses.StoriesResponse;
import com.mantra.ionnews.ui.customui.flipview.FlipView;
import com.mantra.ionnews.ui.dialogs.NewsFooterMenuBottomSheetDialog;
import com.mantra.ionnews.ui.dialogs.NewsPopupDialogs;
import com.mantra.ionnews.utils.CustomShareIntent;
import com.mantra.ionnews.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.mantra.ionnews.utils.AppConstants.KEY_CATEGORY_ID;
import static com.mantra.ionnews.utils.AppConstants.KEY_CATEGORY_NAME;
import static com.mantra.ionnews.utils.AppConstants.KEY_CATEGORY_STORIES;
import static com.mantra.ionnews.utils.AppConstants.KEY_LIKED_STORIES;
import static com.mantra.ionnews.utils.AppConstants.KEY_LIKED_STORY_INDEX;
import static com.mantra.ionnews.utils.AppConstants.KEY_STORY_ITEM;
import static com.mantra.ionnews.utils.ConstantClass.NEWS_DETAIL;

/**
 * Created by rajat on 30/03/17.
 */

public class NewsDetailActivity extends BaseActivity
        implements BaseResponseInterface, OnNewsFlipItemClickListener, OnLikeStoryResponseListener,
        WebViewActivity.OnLikeUpdatedListener, OnPopupDialogClickListener,
        NewsFooterMenuBottomSheetDialog.OnLikeUpdatedListener {

    private String TAG = NEWS_DETAIL;

    private FlipView flipView;
    private LinearLayout mainView;

    private String categoryId, categoryName;
    private int webViewIndex = 0;
    private CategoryStoryResponse categoryStoryResponse;
    private List<Story> storyList = new ArrayList<>();

    private FlipAdapter flipAdapter;
    private boolean isLikeView = false;
    private int likedStoryIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        categoryId = getIntent().getExtras().getString(KEY_CATEGORY_ID);
        categoryName = getIntent().getExtras().getString(KEY_CATEGORY_NAME);

        initView();

        if (categoryId != null) {
            isLikeView = false;
            StoriesResponse storiesResponse = (StoriesResponse) getIntent().getExtras().getBundle(KEY_CATEGORY_STORIES).getSerializable(KEY_CATEGORY_STORIES);
            storyList.addAll(storiesResponse.getCategoryStories());
            setFlipView();
            fetchStory(-1);
        } else {
            isLikeView = true;
            Bundle bundle = getIntent().getBundleExtra(KEY_LIKED_STORIES);
            storyList = (List<Story>) bundle.getSerializable(KEY_LIKED_STORIES);
            likedStoryIndex = bundle.getInt(KEY_LIKED_STORY_INDEX);
            setFlipView();
        }

        WebViewActivity.onLikeUpdatedListener = this;
        NewsFooterMenuBottomSheetDialog.onLikeUpdatedListener = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void fetchStory(final int page) {
        if (Util.hasInternetAccess(this)) {
            CategoryStoryDataHandler categoryStoryDataHandler = new CategoryStoryDataHandler(this, getApplicationContext());
            categoryStoryDataHandler.request(categoryId, page);
            if (page == -1) showProgressDialog(getString(R.string.loading));
        } else {
            showNetworkErrorMessage(
                    mainView,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fetchStory(page);
                        }
                    },
                    this
            );
        }
    }

    private void initView() {
        flipView = (FlipView) findViewById(R.id.and_flip_view);
        mainView = (LinearLayout) findViewById(R.id.and_main_view);
    }

    @Override
    public void response(Object response, Error error) {
        hideProgressDialog();
        if (error == null) {
            categoryStoryResponse = (CategoryStoryResponse) response;
            if (storyList.get(0).getIslike() == null) storyList.clear();

            storyList.addAll(categoryStoryResponse.getData().getAllData());
            setFlipView();
        } else {
            if (error.getMessage().equalsIgnoreCase(getString(R.string.error_no_internet_connection))) {
                showSnackbarMessage(
                        mainView,
                        getString(R.string.error_no_internet_connection),
                        true,
                        getString(R.string.retry),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (categoryStoryResponse == null) fetchStory(-1);
                                else fetchStory(categoryStoryResponse.getCurrentPage() + 1);
                            }
                        },
                        false
                );
            } else {
                showToastMessage(error.getMessage(), false);
                finish();
            }
        }
    }

    private void setFlipView() {
        if (!isLikeView) {
            if (categoryStoryResponse == null) {
                flipAdapter = new FlipAdapter(getApplicationContext(), storyList, categoryName, this, -1);
                flipView.setAdapter(flipAdapter);

            } else {

                if (categoryStoryResponse.getCurrentPage() == 1) {
                    flipAdapter = new FlipAdapter(getApplicationContext(), storyList, categoryName, this, categoryStoryResponse.getPerPage());
                    flipView.setAdapter(flipAdapter);
                } else {
                    flipAdapter.notifyDataSetChanged();
                }
            }
        } else {
            flipAdapter = new FlipAdapter(getApplicationContext(), storyList, getString(R.string.likes), this, storyList.size());
            flipView.setAdapter(flipAdapter);
            flipView.flipTo(likedStoryIndex);
        }
    }

    @Override
    public void onNewsItemClick(int position, ImageView imageView) {
        webViewIndex = position;

        if (storyList.get(position).getCrawlUrl() != null && !storyList.get(position).getCrawlUrl().isEmpty()) {
            Intent webViewIntent = new Intent(this, WebViewActivity.class);
            webViewIntent.putExtra(KEY_STORY_ITEM, storyList.get(position));
            startActivity(webViewIntent);
        } else if (storyList.get(position).getCustomUrl() != null && !storyList.get(position).getCustomUrl().isEmpty()) {
            Intent webViewIntent = new Intent(this, WebViewActivity.class);
            webViewIntent.putExtra(KEY_STORY_ITEM, storyList.get(position));
            startActivity(webViewIntent);
        } else {
            showToastMessage("Invalid web URL!", false);
        }
    }

    public void onShareClick(int position, ImageView imageView) {
        new CustomShareIntent(
                this,
                imageView,
                storyList.get(position).getCrawlUrl(),
                "Check out this story: " + storyList.get(position).getCrawlUrl() + " from ION News!"
        ).share();
    }

    @Override
    public void onLikeClick(int position) {
        LikeStoryRequest likeStoryRequest = new LikeStoryRequest();
        likeStoryRequest.setContentId(storyList.get(position).getId());
        initLikeStoryRequest(likeStoryRequest, position);
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

    @Override
    public void onBackClick() {
        finish();
        overridePendingTransition(0, R.anim.slide_out_down);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_down);
    }

    @Override
    public void onPaginationIndexFlip() {
        if (!isLikeView && categoryStoryResponse.getCurrentPage() != categoryStoryResponse.getLastPage())
            fetchStory(categoryStoryResponse.getCurrentPage() + 1);
    }

    @Override
    public void onHeaderMenuClick(int position) {
        NewsPopupDialogs newsPopupDialogs = new NewsPopupDialogs(this);
        newsPopupDialogs.openHeaderOptionsPopup(mainView);
    }

    @Override
    public void onFooterMenuClick(int position) {
        Story storyToMenu = storyList.get(position);

        BottomSheetDialogFragment bottomSheetDialogFragment = NewsFooterMenuBottomSheetDialog.newInstance(storyToMenu);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    @Override
    public void onLastItem() {
        if (categoryId != null
                && categoryStoryResponse != null
                && categoryStoryResponse.getCurrentPage() != categoryStoryResponse.getLastPage()
                && categoryStoryResponse.getLastPage() != 0)
            fetchStory(-1);
    }

    @Override
    public void likeStoryResponse(String message, Error error, int index) {
        if (error == null) {

        } else {
            if (storyList.get(index).getIslike() == 1)
                storyList.get(index).setIslike(0);
            else storyList.get(index).setIslike(1);

            flipAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLikeUpdated(boolean isLiked) {
        if (isLiked) storyList.get(webViewIndex).setIslike(1);
        else storyList.get(webViewIndex).setIslike(0);
        flipAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackToTopClick() {
        flipView.flipTo(0);
    }
}
