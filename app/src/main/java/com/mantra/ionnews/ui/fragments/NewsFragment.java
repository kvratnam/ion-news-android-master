package com.mantra.ionnews.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mantra.ionnews.R;
import com.mantra.ionnews.adapters.DynamicTabsPagerAdapter;
import com.mantra.ionnews.models.DashboardRequest;
import com.mantra.ionnews.models.FragmentClick;
import com.mantra.ionnews.models.FragmentState;
import com.mantra.ionnews.models.User;
import com.mantra.ionnews.models.responses.StoriesResponse;
import com.mantra.ionnews.ui.activities.DashboardActivity;
import com.mantra.ionnews.ui.customui.SlidingTabLayout;
import com.mantra.ionnews.utils.LocalStorage;
import com.mantra.ionnews.utils.ScrollSwipeRefreshLayout;
import com.mantra.ionnews.utils.Util;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import de.greenrobot.event.EventBus;

import static com.mantra.ionnews.utils.ConstantClass.NEWS;
import static com.mantra.ionnews.utils.ConstantClass.ON_GOTO_PROFILE_FRAGMENT;
import static com.mantra.ionnews.utils.ConstantClass.STORIES_RESPONSE;
import static com.mantra.ionnews.utils.ConstantClass.WITH_LIKES;
import static com.mantra.ionnews.utils.ConstantClass.WITH_STORIES;

/**
 * Created by TaNMay on 30/03/17.
 */

public class NewsFragment extends BaseFragment implements View.OnClickListener {

    public final static String ARG_PARAM1 = "STORIES_LIST";
    public static String TAG = NEWS + "==>";

    private ScrollSwipeRefreshLayout swipeRefreshLayout;
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView ivNewsGrid;
    private TextView tvProfile;
    private RelativeLayout profileSec;

    private DynamicTabsPagerAdapter dynamicTabsPagerAdapter;
    private List<StoriesResponse> storiesResponseList;
    private User user;

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Bitmap circularBitmap = Util.getCircularBitmap(bitmap);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                tvProfile.setBackground(new BitmapDrawable(getContext().getResources(), circularBitmap));
            } else {
                tvProfile.setBackgroundDrawable(new BitmapDrawable(getContext().getResources(), circularBitmap));
            }
        }

        @Override
        public void onBitmapFailed(final Drawable errorDrawable) {
            setDefaultProfileImage();
        }

        @Override
        public void onPrepareLoad(final Drawable placeHolderDrawable) {

        }
    };

    public NewsFragment() {

    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_news, container, false);

        registerEventBus();

        user = LocalStorage.getInstance(getContext()).getUser();

        initView(layout);
        updateFragment();

        setUpHeader();

        storiesResponseList = LocalStorage.getInstance(getContext()).getStories();
        setUpTabLayout();

        return layout;
    }

    private void setUpHeader() {
        setUserProfilePicture();

        ivNewsGrid.setOnClickListener(this);
        profileSec.setOnClickListener(this);
    }

    private void setUserProfilePicture() {
        if (user.getProfileImg() != null && !user.getProfileImg().isEmpty()) {
            Picasso.with(getContext())
                    .load(user.getProfileImg())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(target);
            tvProfile.setTag(target);
        } else {
            setDefaultProfileImage();
        }
    }

    private void setDefaultProfileImage() {
        tvProfile.setText(user.getFirstName().substring(0, 1).toUpperCase());
    }

    private void setUpTabLayout() {
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16 * 2, getResources().getDisplayMetrics());

        dynamicTabsPagerAdapter = new DynamicTabsPagerAdapter(getFragmentManager(), storiesResponseList, getContext());
        viewPager.setAdapter(dynamicTabsPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageMargin(-margin);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);


            }
        });

        tabLayout.setCustomTabView(R.layout.custom_tab_icon, R.id.cti_title);
        tabLayout.setViewPager(viewPager);
    }

    private void enableDisableSwipeRefresh(boolean enable) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(enable);
        }
    }

    private void initView(View rootView) {
        tabLayout = (SlidingTabLayout) rootView.findViewById(R.id.fn_tab_layout);
        viewPager = (ViewPager) rootView.findViewById(R.id.fn_view_pager);
        ivNewsGrid = (ImageView) rootView.findViewById(R.id.fn_news_grid);
        tvProfile = (TextView) rootView.findViewById(R.id.fn_profile);
        profileSec = (RelativeLayout) rootView.findViewById(R.id.fn_profile_sec);
        swipeRefreshLayout = (ScrollSwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

        swipeRefreshLayout.setScrollUpChild(viewPager);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshView();
            }
        });
    }

    private void refreshView() {
        ((DashboardActivity) getActivity()).fetchStories();
    }

    private void updateFragment() {
        EventBus.getDefault().postSticky(new FragmentState(NEWS));
    }

    protected void registerEventBus() {
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fn_news_grid:
                openNewsGrid();
                break;
            case R.id.fn_profile_sec:
                openProfileFragment();
                break;
        }
    }

    private void openProfileFragment() {
        EventBus.getDefault().postSticky(new FragmentClick(ON_GOTO_PROFILE_FRAGMENT, WITH_LIKES));
    }

    private void openNewsGrid() {
        EventBus.getDefault().postSticky(new FragmentClick(ON_GOTO_PROFILE_FRAGMENT, WITH_STORIES));
    }

    public void onEvent(DashboardRequest dashboardRequest) {
        Log.d(TAG, dashboardRequest.getStoriesResponse());
        if (dashboardRequest.getStoriesResponse().equals(STORIES_RESPONSE)) {
            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
            storiesResponseList = LocalStorage.getInstance(getContext()).getStories();
            setUpTabLayout();
//            dynamicTabsPagerAdapter.notifyDataSetChanged();
        }
    }
}
