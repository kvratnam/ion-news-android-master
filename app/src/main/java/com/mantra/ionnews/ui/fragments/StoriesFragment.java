package com.mantra.ionnews.ui.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.mantra.ionnews.R;
import com.mantra.ionnews.models.Story;
import com.mantra.ionnews.models.responses.StoriesResponse;
import com.mantra.ionnews.ui.activities.NewsDetailActivity;
import com.mantra.ionnews.utils.Util;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;

import static com.mantra.ionnews.utils.AppConstants.KEY_CATEGORY_ID;
import static com.mantra.ionnews.utils.AppConstants.KEY_CATEGORY_NAME;
import static com.mantra.ionnews.utils.AppConstants.KEY_CATEGORY_STORIES;
import static com.mantra.ionnews.utils.ConstantClass.STORIES;

/**
 * Created by rajat on 30/03/17.
 */

public class StoriesFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "STORIES_RESPONSE";

    private String TAG = STORIES + " ==>";

    private RelativeLayout mainView;
    private View translucentView;
    private ImageView ivMainImage;
    private TextView tvSource, tvTitle, tvAuthor, tvHashtags;

    private int viewHeight = 0;

    private StoriesResponse storiesResponse;

    public static StoriesFragment newInstance(StoriesResponse storiesResponse) {
        StoriesFragment fragment = new StoriesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, storiesResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            storiesResponse = (StoriesResponse) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_stories, container, false);

        initView(layout);

        setUpMainStory();

        return layout;
    }

    private void setUpMainStory() {
        Story mainStory = storiesResponse.getCategoryStories().get(0);
        if (mainStory.getImage() != null && !mainStory.getImage().isEmpty()) {
            OkHttpClient client = new OkHttpClient();
            Picasso.Builder builder = new Picasso.Builder(getContext());
            builder.downloader(new OkHttp3Downloader(client))
                    .build()
                    .load(mainStory.getImage())
                    .into(ivMainImage);

            translucentView.setVisibility(View.VISIBLE);
        }
//        if (mainStory.getSource() != null) tvSource.setText(mainStory.getSource());
        if (mainStory.getTitle() != null && !mainStory.getTitle().isEmpty())
            tvTitle.setText(mainStory.getTitle());
        if (mainStory.getAuthor() != null && !mainStory.getAuthor().isEmpty())
            tvAuthor.setText(mainStory.getAuthor());
        if (mainStory.getTags() != null)
            tvHashtags.setText(Util.getFormattedHashtags(mainStory.getTags(), storiesResponse.getCategoryTitle()));

        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStoryItemClick();
            }
        });
    }

    private void onStoryItemClick() {
        openNewsDetailActivity();
    }

    private void openNewsDetailActivity() {
        Bundle storiesResponseBundle = new Bundle();
        storiesResponseBundle.putSerializable(KEY_CATEGORY_STORIES, storiesResponse);

        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        intent.putExtra(KEY_CATEGORY_NAME, storiesResponse.getCategoryTitle() + "");
        intent.putExtra(KEY_CATEGORY_ID, storiesResponse.getCategoryStories().get(0).getCategoryId() + "");
        intent.putExtra(KEY_CATEGORY_STORIES, storiesResponseBundle);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_up, 0);

    }

    private void initView(View rootView) {
        mainView = (RelativeLayout) rootView.findViewById(R.id.fs_main_view);
        ivMainImage = (ImageView) rootView.findViewById(R.id.fs_main_image);
        tvSource = (TextView) rootView.findViewById(R.id.fs_source);
        tvTitle = (TextView) rootView.findViewById(R.id.fs_title);
        tvAuthor = (TextView) rootView.findViewById(R.id.fs_author);
        tvHashtags = (TextView) rootView.findViewById(R.id.fs_hashtags);
        translucentView = rootView.findViewById(R.id.fs_translucent_view);
        translucentView.setVisibility(View.GONE);

        Typeface titleTypeface = Typeface.createFromAsset(getContext().getAssets(), "SourceSerifPro-Bold.ttf");
        tvTitle.setTypeface(titleTypeface);

        setUpTranslucentView();
    }

    private void setUpTranslucentView() {
        ViewTreeObserver viewTreeObserver = mainView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        mainView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    else mainView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    viewHeight = mainView.getHeight();

                    ViewGroup.LayoutParams params = translucentView.getLayoutParams();
                    params.height = (int) (viewHeight * 0.3);
                    translucentView.setLayoutParams(params);

                }
            });
        }
    }
}

