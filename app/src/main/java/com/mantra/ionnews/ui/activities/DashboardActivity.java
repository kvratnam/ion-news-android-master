package com.mantra.ionnews.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.mantra.ionnews.R;
import com.mantra.ionnews.adapters.ViewPagerAdapter;
import com.mantra.ionnews.datahandlers.StoriesDataHandler;
import com.mantra.ionnews.interfaces.BaseResponseInterface;
import com.mantra.ionnews.interfaces.OnFragmentEventTagListener;
import com.mantra.ionnews.models.DashboardRequest;
import com.mantra.ionnews.models.FragmentClick;
import com.mantra.ionnews.models.FragmentState;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.ui.fragments.EditProfileFragment;
import com.mantra.ionnews.ui.fragments.SettingsFragment;
import com.mantra.ionnews.utils.LocalStorage;
import com.mantra.ionnews.utils.Util;

import de.greenrobot.event.EventBus;

import static com.mantra.ionnews.utils.ConstantClass.DASHBOARD;
import static com.mantra.ionnews.utils.ConstantClass.EDIT_PROFILE;
import static com.mantra.ionnews.utils.ConstantClass.ON_GOTO_NEWS_FRAGMENT;
import static com.mantra.ionnews.utils.ConstantClass.ON_GOTO_PROFILE_FRAGMENT;
import static com.mantra.ionnews.utils.ConstantClass.PROFILE;
import static com.mantra.ionnews.utils.ConstantClass.SETTINGS;
import static com.mantra.ionnews.utils.ConstantClass.STORIES_RESPONSE;
import static com.mantra.ionnews.utils.ConstantClass.WITH_LIKES;

/**
 * Created by rajat on 17/03/17.
 */

public class DashboardActivity extends BaseActivity implements BaseResponseInterface {

    public static OnFragmentEventTagListener onFragmentEventTagListener;

    private String TAG = DASHBOARD;

    private ViewPager viewPager;

    private boolean isFromNotification = false;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        registerEventBus();
        doubleBackToExitPressedOnce = false;

        initView();
        setUpViewPager();

        fetchStories();
    }

    private void setUpViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(1);
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.ad_view_pager);
    }

    protected void registerEventBus() {
        EventBus.getDefault().registerSticky(this);
    }

    public void onEvent(FragmentState fragmentState) {
        Log.d(TAG, fragmentState.getVisibleFragment());
        if (fragmentState.getVisibleFragment().equals(PROFILE)) {

        }
    }

    public void onEvent(FragmentClick fragmentClick) {
        Log.d(TAG, fragmentClick.getViewClicked());
        if (fragmentClick.getViewClicked().equals(ON_GOTO_NEWS_FRAGMENT)) {
            viewPager.setCurrentItem(1);
        } else if (fragmentClick.getViewClicked().equals(ON_GOTO_PROFILE_FRAGMENT)) {
            if (fragmentClick.getTag().equals(WITH_LIKES))
                onFragmentEventTagListener.onProfileGridTypeChange(false);
            else onFragmentEventTagListener.onProfileGridTypeChange(true);
            viewPager.setCurrentItem(0);
        }
    }

    public void fetchStories() {
        StoriesDataHandler storiesDataHandler = new StoriesDataHandler(this, this);
        if (Util.hasInternetAccess(this)) {
            storiesDataHandler.request();
            if (LocalStorage.getInstance(this).getStories().size() == 0)
                showProgressDialog(getString(R.string.loading));
        } else
            showNetworkErrorMessage(
                    findViewById(R.id.ad_main_view),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fetchStories();
                        }
                    },
                    this
            );
    }

    @Override
    public void response(Object response, Error error) {
        hideProgressDialog();
        if (error == null) {
            EventBus.getDefault().postSticky(new DashboardRequest(STORIES_RESPONSE));
        } else {
            showToastMessage(error.getMessage(), false);
        }
    }

    public void openThisFragment(String fragmentTag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentTag.equals(SETTINGS)) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                    .replace(R.id.ad_fragment_container, SettingsFragment.newInstance(), SETTINGS)
                    .addToBackStack(DASHBOARD)
                    .commit();
        } else if (fragmentTag.equals(EDIT_PROFILE)) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                    .replace(R.id.ad_fragment_container, EditProfileFragment.newInstance(), EDIT_PROFILE)
                    .addToBackStack(DASHBOARD)
                    .commit();
        } else {
            Log.e(TAG, "Fragment error!");
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "getBackStackEntryCount: " + getSupportFragmentManager().getBackStackEntryCount());
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
//            if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
//                return;
//            }
//            this.doubleBackToExitPressedOnce = true;
//            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    doubleBackToExitPressedOnce = false;
//                }
//            }, 2000);
        }
    }

}
