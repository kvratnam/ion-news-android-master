package com.mantra.ionnews.ui.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.mantra.ionnews.R;
import com.mantra.ionnews.adapters.OptionsAdapter;
import com.mantra.ionnews.datahandlers.LikeStoryDataHandler;
import com.mantra.ionnews.interfaces.OnLikeStoryResponseListener;
import com.mantra.ionnews.interfaces.OnOptionsClickListener;
import com.mantra.ionnews.models.Story;
import com.mantra.ionnews.models.requests.LikeStoryRequest;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.ui.activities.WebViewActivity;
import com.mantra.ionnews.utils.CustomShareIntent;
import com.mantra.ionnews.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import static com.mantra.ionnews.utils.AppConstants.KEY_STORY_ITEM;
import static com.mantra.ionnews.utils.AppConstants.VAL_ADS;
import static com.mantra.ionnews.utils.ConstantClass.NEWS_DETAIL_FOOTER_MENU;

/**
 * Created by rajat on 16/03/17.
 */

public class NewsFooterMenuBottomSheetDialog extends BaseBottomSheetDialog
        implements OnOptionsClickListener, OnLikeStoryResponseListener {

    public final static String ARG_PARAM1 = "STORY_TO_MENU";
    public static OnLikeUpdatedListener onLikeUpdatedListener;
    private String TAG = NEWS_DETAIL_FOOTER_MENU + " ==>";
    private RecyclerView optionsRv;
    private RecyclerView.Adapter optionsAdapter;
    private RecyclerView.LayoutManager optionsLayoutManager;

    private Story story;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    public static NewsFooterMenuBottomSheetDialog newInstance(Story storyToMenu) {
        NewsFooterMenuBottomSheetDialog bottomSheetFragment = new NewsFooterMenuBottomSheetDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PARAM1, storyToMenu);
        bottomSheetFragment.setArguments(bundle);

        return bottomSheetFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            story = (Story) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_news_footer_menu, null);
        initView(contentView);
        dialog.setContentView(contentView);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        ((BottomSheetBehavior) behavior).setPeekHeight(1000);

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        setUpOptionsRv();
    }

    private void setUpOptionsRv() {
        optionsLayoutManager = new LinearLayoutManager(getContext());
        optionsAdapter = new OptionsAdapter(this, story, story.getType().equalsIgnoreCase(VAL_ADS));
        optionsRv.setLayoutManager(optionsLayoutManager);
        optionsRv.setAdapter(optionsAdapter);
    }

    private void initView(View contentView) {
        optionsRv = (RecyclerView) contentView.findViewById(R.id.bsnfm_options_rv);
    }

    @Override
    public void onOptionClick(View view, boolean isAd) {
        int position = optionsRv.getChildAdapterPosition(view);
        if (isAd) {
            switch (position) {
                case 0:
                    onWebViewOptionClick();
                    break;
            }
        } else {
            switch (position) {
                case 0:
                    onLikeOptionClick();
                    break;
                case 1:
                    onShareOptionClick();
                    break;
                case 2:
                    onWebViewOptionClick();
                    break;
            }
        }
    }

    private void onWebViewOptionClick() {
        dismiss();
        if (story.getCrawlUrl() != null && !story.getCrawlUrl().isEmpty()) {
            Intent webViewIntent = new Intent(getActivity(), WebViewActivity.class);
            webViewIntent.putExtra(KEY_STORY_ITEM, story);
            startActivity(webViewIntent);
        } else {
            showToastMessage("Invalid web URL!", false);
        }
    }

    private void onShareOptionClick() {
        if (story.getImage() != null && !story.getImage().isEmpty())
            new CustomShareIntent(
                    getContext(),
                    story.getImage(),
                    story.getCrawlUrl(),
                    "Check out this story: " + story.getCrawlUrl() + " from ION News!"
            ).share();
        else {
            String url = null;
            new CustomShareIntent(
                    getContext(),
                    url,
                    story.getCrawlUrl(),
                    "Check out this story: " + story.getCrawlUrl() + " from ION News!"
            ).share();
        }
        dismiss();
    }

    private void onLikeOptionClick() {
        if (story.getIslike() != null) {
            if (story.getIslike() == 1) {
                story.setIslike(0);
                onLikeUpdatedListener.onLikeUpdated(false);
            } else {
                story.setIslike(1);
                onLikeUpdatedListener.onLikeUpdated(true);
            }
            optionsAdapter.notifyDataSetChanged();
            LikeStoryRequest likeStoryRequest = new LikeStoryRequest();
            likeStoryRequest.setContentId(story.getId());
            initLikeStoryRequest(likeStoryRequest);
        }
        dismiss();
    }

    private void initLikeStoryRequest(final LikeStoryRequest likeStoryRequest) {
        if (Util.hasInternetAccess(getContext())) {
            likeStory(likeStoryRequest);
        } else {
            showNetworkErrorMessage(
                    getActivity().findViewById(R.id.and_main_view),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            likeStory(likeStoryRequest);
                        }
                    },
                    getContext()
            );
        }
    }

    private void likeStory(LikeStoryRequest likeStoryRequest) {
        Gson gson = new Gson();
        try {
            JSONObject likeStoryJson = new JSONObject(gson.toJson(likeStoryRequest));
            LikeStoryDataHandler likeStoryDataHandler = new LikeStoryDataHandler(this, getContext());
            likeStoryDataHandler.request(likeStoryJson, -1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void likeStoryResponse(String message, Error error, int index) {
        if (error == null) {

        } else {
            if (story.getIslike() == 1)
                story.setIslike(0);
            else story.setIslike(1);

            optionsAdapter.notifyDataSetChanged();
        }
    }

    public interface OnLikeUpdatedListener {

        void onLikeUpdated(boolean isLiked);
    }
}
