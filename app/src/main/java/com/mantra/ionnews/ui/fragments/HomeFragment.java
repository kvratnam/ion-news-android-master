package com.mantra.ionnews.ui.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.mantra.ionnews.R;
import com.mantra.ionnews.adapters.HomeFragmentAdapter;
import com.mantra.ionnews.datahandlers.GetAllLikesDataHandler;
import com.mantra.ionnews.interfaces.OnFragmentEventTagListener;
import com.mantra.ionnews.interfaces.OnGetAllLikesResponseListener;
import com.mantra.ionnews.interfaces.OnProfileGridItemClickListener;
import com.mantra.ionnews.interfaces.OnProfileUpdateListener;
import com.mantra.ionnews.interfaces.SearchImpl;
import com.mantra.ionnews.models.DashboardRequest;
import com.mantra.ionnews.models.FragmentClick;
import com.mantra.ionnews.models.FragmentState;
import com.mantra.ionnews.models.Story;
import com.mantra.ionnews.models.User;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.models.responses.GetAllLikesResponse;
import com.mantra.ionnews.models.responses.StoriesResponse;
import com.mantra.ionnews.ui.activities.DashboardActivity;
import com.mantra.ionnews.ui.activities.NewsDetailActivity;
import com.mantra.ionnews.ui.customui.GridDividerDecoration;
import com.mantra.ionnews.utils.LocalStorage;
import com.mantra.ionnews.utils.Util;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.mantra.ionnews.utils.AppConstants.KEY_CATEGORY_ID;
import static com.mantra.ionnews.utils.AppConstants.KEY_CATEGORY_NAME;
import static com.mantra.ionnews.utils.AppConstants.KEY_CATEGORY_STORIES;
import static com.mantra.ionnews.utils.AppConstants.KEY_LIKED_STORIES;
import static com.mantra.ionnews.utils.AppConstants.KEY_LIKED_STORY_INDEX;
import static com.mantra.ionnews.utils.ConstantClass.EDIT_PROFILE;
import static com.mantra.ionnews.utils.ConstantClass.HOME;
import static com.mantra.ionnews.utils.ConstantClass.ON_GOTO_NEWS_FRAGMENT;
import static com.mantra.ionnews.utils.ConstantClass.PROFILE;
import static com.mantra.ionnews.utils.ConstantClass.SETTINGS;
import static com.mantra.ionnews.utils.ConstantClass.STORIES_RESPONSE;

public class HomeFragment extends BaseFragment
        implements AppBarLayout.OnOffsetChangedListener, OnProfileGridItemClickListener,
        OnGetAllLikesResponseListener, OnFragmentEventTagListener, OnProfileUpdateListener,SearchImpl {

    int initVerticalOffset = 0;

    private String TAG = PROFILE + " ==>";

    private LinearLayout likesSec, storiesSec;
    private TextView tvProfile, tvName, tvLikes, tvStories, tvLikesHint, tvStoriesHint;
    private ImageView ivNews, ivNewsArrow, ivSettings;
    private AppBarLayout mAppBarLayout;
    private RecyclerView profileGridRv;
    private SwipeRefreshLayout refreshLayout;

    private GetAllLikesResponse likesResponse;
    private List<Story> likesItemList = new ArrayList<>();
    private List<StoriesResponse> storiesResponseList = new ArrayList<>();
    private User user;
    private EditText editTextHeader;


    private boolean isLikeView = false, isDecorationAdded = false;

    private RecyclerView.Adapter homeFragmentAdapter;
    private RecyclerView.LayoutManager profileGridLayoutManager;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            try {
                Bitmap circularBitmap = Util.getCircularBitmap(bitmap);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tvProfile.setBackground(new BitmapDrawable(getContext().getResources(), circularBitmap));
                } else {
                    tvProfile.setBackgroundDrawable(new BitmapDrawable(getContext().getResources(), circularBitmap));
                }
                tvProfile.setText("");
            } catch (Exception e) {

            }

        }

        @Override
        public void onBitmapFailed(final Drawable errorDrawable) {
            setDefaultProfileImage();
        }

        @Override
        public void onPrepareLoad(final Drawable placeHolderDrawable) {
            setDefaultProfileImage();
        }
    };

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }

        DashboardActivity.onFragmentEventTagListener = this;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // What i have added is this
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.home_fragment, container, false);
        try {
            registerEventBus();
            user = LocalStorage.getInstance(getContext()).getUser();
            EditProfileFragment.onProfileUpdateListener = this;

            initView(layout);
            updateFragment();

            storiesResponseList = LocalStorage.getInstance(getContext()).getStories();
            likesResponse = LocalStorage.getInstance(getContext()).getAllLikes();
            if (likesResponse != null) {
                likesItemList = likesResponse.getData();
                setLikesCount(likesResponse.getTotal());
            }

            fetchAllLikes(-1, false);


            setUpSubHeader();
            setUpProfileGrid();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LocalStorage.getInstance(getContext()).isAnyChangeInLike()) fetchAllLikes(-1, true);
    }

    private void fetchAllLikes(final int page, final boolean isPullDown) {
        if (Util.hasInternetAccess(getContext())) {
            GetAllLikesDataHandler getAllLikesDataHandler = new GetAllLikesDataHandler(this, getContext());
            getAllLikesDataHandler.request(page, isPullDown);
            if (page == -1) {
//                if (!isPullDown) showProgressDialog(getString(R.string.loading));
                refreshLayout.setRefreshing(true);
            }
        } else {
            if (refreshLayout != null) refreshLayout.setRefreshing(false);
            showNetworkErrorMessage(
                    profileGridRv,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fetchAllLikes(page, isPullDown);
                        }
                    },
                    getContext()
            );
        }
    }

    private void setUpProfileGrid() {
        try {
            updateSubHeaderDetails();

            profileGridLayoutManager = new GridLayoutManager(getContext(), 1, LinearLayoutManager.VERTICAL, false);
            if (isLikeView)
                homeFragmentAdapter = new HomeFragmentAdapter(null, likesItemList, this);
            else
                homeFragmentAdapter = new HomeFragmentAdapter(storiesResponseList, null, this);
            profileGridRv.setLayoutManager(profileGridLayoutManager);
            if (!isDecorationAdded) {
                isDecorationAdded = true;
                profileGridRv.addItemDecoration(new GridDividerDecoration(getResources().getDimensionPixelSize(R.dimen.likes_item_margin), 2));
            }
            profileGridRv.setAdapter(homeFragmentAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSubHeaderDetails() {
        if (isLikeView) {
            tvLikes.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
            tvLikesHint.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
            tvStories.setTextColor(ContextCompat.getColor(getActivity(), R.color.profileSubHeaderText));
            tvStoriesHint.setTextColor(ContextCompat.getColor(getActivity(), R.color.profileSubHeaderText));
        } else {
            tvLikes.setTextColor(ContextCompat.getColor(getActivity(), R.color.profileSubHeaderText));
            tvLikesHint.setTextColor(ContextCompat.getColor(getActivity(), R.color.profileSubHeaderText));
            tvStories.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
            tvStoriesHint.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
        }
    }


    private void setUpSubHeader() {
        tvName.setText(user.getFirstName().substring(0, 1).toUpperCase()
                + user.getFirstName().substring(1)
                + " "
                + user.getLastName().substring(0, 1).toUpperCase()
                + user.getLastName().substring(1)
        );

        setStoriesCount(storiesResponseList.size());

        storiesSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLikeView = false;
                setUpProfileGrid();
            }
        });

        likesSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLikeView = true;
                setUpProfileGrid();
            }
        });
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

    private void openSettings() {
        ((DashboardActivity) getActivity()).openThisFragment(SETTINGS);
    }

    private void setDefaultProfileImage() {
        tvProfile.setText(user.getFirstName().substring(0, 1).toUpperCase());
    }

    private void setLikesCount(int likesCount) {
        tvLikes.setText(likesCount + "");
    }

    private void setStoriesCount(int storiesCount) {
        tvStories.setText(storiesCount + "");
    }

    private void initView(View rootView) {
        tvProfile = (TextView) rootView.findViewById(R.id.fp_profile);
        ivSettings = (ImageView) rootView.findViewById(R.id.fp_settings);
        ivNews = (ImageView) rootView.findViewById(R.id.fp_news);
        ivNewsArrow = (ImageView) rootView.findViewById(R.id.fp_news_arrow);
        tvName = (TextView) rootView.findViewById(R.id.fp_name);
        mAppBarLayout = (AppBarLayout) rootView.findViewById(R.id.fp_app_bar_layout);
        tvLikes = (TextView) rootView.findViewById(R.id.fp_profile_detail_likes);
        tvStories = (TextView) rootView.findViewById(R.id.fp_profile_detail_stories);
        tvLikesHint = (TextView) rootView.findViewById(R.id.fp_profile_detail_likes_hint);
        tvStoriesHint = (TextView) rootView.findViewById(R.id.fp_profile_detail_stories_hint);
        likesSec = (LinearLayout) rootView.findViewById(R.id.fp_profile_detail_likes_sec);
        storiesSec = (LinearLayout) rootView.findViewById(R.id.fp_profile_detail_stories_sec);
        profileGridRv = (RecyclerView) rootView.findViewById(R.id.fp_likes_rv);
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fp_swipe_refresh_layout);

        Typeface subHeaderTypeface = Typeface.createFromAsset(getContext().getAssets(), "ChronicaPro-Medium.ttf");
        tvLikes.setTypeface(subHeaderTypeface);
        tvStories.setTypeface(subHeaderTypeface);
        tvLikesHint.setTypeface(subHeaderTypeface);
        tvStoriesHint.setTypeface(subHeaderTypeface);

        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isLikeView) fetchAllLikes(-1, true);
                else {
                    ((DashboardActivity) getActivity()).fetchStories();
                    refreshLayout.setRefreshing(true);
                }
            }
        });
        searchView =(SearchView)  rootView.findViewById(R.id.search);
        editTextHeader =(EditText)  rootView.findViewById(R.id.header);

        editTextHeader.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                   /* editTextHeader.setVisibility(View.GONE);
                    searchView.setVisibility(View.VISIBLE);*/
                }
                return false;
            }
        });

    }

    private void updateFragment() {
        EventBus.getDefault().postSticky(new FragmentState(HOME));
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
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.d(TAG, "verticalOffset: " + verticalOffset);
        if (verticalOffset != initVerticalOffset) {
            initVerticalOffset = verticalOffset;
            ViewGroup.LayoutParams params = tvProfile.getLayoutParams();
            int appBarLayoutHeight = appBarLayout.getHeight();
            double dimen = (appBarLayoutHeight + verticalOffset) * 0.6;
            params.height = (int) dimen;
            params.width = (int) dimen;
            tvProfile.setLayoutParams(params);
        }
    }

    @Override
    public void onProfileGridItemClick(View view, boolean isLikeItem) {
        int position = profileGridRv.getChildAdapterPosition(view);
        if (isLikeItem) {
            /*Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY_LIKED_STORIES, (Serializable) likesItemList);
            bundle.putInt(KEY_LIKED_STORY_INDEX, position);
            intent.putExtra(KEY_LIKED_STORIES, bundle);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_up, 0);*/

            NewsdetailsFragment ldf = new NewsdetailsFragment ();
            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY_LIKED_STORIES, (Serializable) likesItemList);
            bundle.putInt(KEY_LIKED_STORY_INDEX, position);
            ldf.setArguments(bundle);
            getFragmentManager().beginTransaction().add(R.id.ad_fragment_container, ldf).commit();


        } else {
            /*Bundle storiesResponseBundle = new Bundle();
            storiesResponseBundle.putSerializable(KEY_CATEGORY_STORIES, storiesResponseList.get(position));
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            intent.putExtra(KEY_CATEGORY_NAME, storiesResponseList.get(position).getCategoryTitle() + "");
            intent.putExtra(KEY_CATEGORY_ID, storiesResponseList.get(position).getCategoryStories().get(0).getCategoryId() + "");
            intent.putExtra(KEY_CATEGORY_STORIES, storiesResponseBundle);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_up, 0);*/



            NewsdetailsFragment ldf = new NewsdetailsFragment ();
            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY_CATEGORY_STORIES, storiesResponseList.get(position));
            bundle.putSerializable(KEY_CATEGORY_NAME,storiesResponseList.get(position).getCategoryTitle() + "");
            bundle.putString(KEY_CATEGORY_ID,storiesResponseList.get(position).getCategoryStories().get(0).getCategoryId() + "");
            ldf.setArguments(bundle);
            getFragmentManager().beginTransaction().add(R.id.ad_fragment_container, ldf).commit();



        }
    }

    @Override
    public void getAllLikesResponse(Object response, Error error, boolean isSwipeRefresh) {
        hideProgressDialog();
        if (refreshLayout != null) refreshLayout.setRefreshing(false);
        if (error == null) {
            if (isSwipeRefresh)
                likesItemList = new ArrayList<>();

            likesResponse = (GetAllLikesResponse) response;
            if (likesResponse.getCurrentPage() == likesResponse.getLastPage() || likesResponse.getLastPage() == 0)
                likesItemList.clear();
            likesItemList.addAll(likesResponse.getData());

            setUpProfileGrid();
            setLikesCount(likesResponse.getTotal());

            if (likesResponse.getCurrentPage() != likesResponse.getLastPage() && likesResponse.getLastPage() != 0) {
                fetchAllLikes(likesResponse.getCurrentPage() + 1, false);
            }
        } else {
            showToastMessage(error.getMessage(), false);
        }
    }

    @Override
    public void onProfileGridTypeChange(boolean isStories) {
        isLikeView = !isStories;
        setUpProfileGrid();
    }

    public void onEvent(DashboardRequest dashboardRequest) {
        Log.d(TAG, dashboardRequest.getStoriesResponse());
        try {

            if (dashboardRequest.getStoriesResponse().equals(STORIES_RESPONSE)) {
                if (refreshLayout != null) refreshLayout.setRefreshing(false);
                storiesResponseList = LocalStorage.getInstance(getContext()).getStories();
                if (!isLikeView)
                    homeFragmentAdapter.notifyDataSetChanged();
                setUpProfileGrid();
                setStoriesCount(storiesResponseList.size());
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProfileUpdated() {
        user = LocalStorage.getInstance(getContext()).getUser();
        setUpSubHeader();
    }

    //Search  Action
    public void searchQuery(String query) {
    }


}