package com.mantra.ionnews.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import com.mantra.ionnews.R;
import com.mantra.ionnews.adapters.ViewPagerAdapter;
import com.mantra.ionnews.datahandlers.SearchByTagHandler;
import com.mantra.ionnews.datahandlers.StoriesDataHandler;
import com.mantra.ionnews.interfaces.BaseResponseInterface;
import com.mantra.ionnews.interfaces.OnFragmentEventTagListener;
import com.mantra.ionnews.models.DashboardRequest;
import com.mantra.ionnews.models.FragmentClick;
import com.mantra.ionnews.models.FragmentState;
import com.mantra.ionnews.models.responses.Error;
import com.mantra.ionnews.ui.fragments.BaseFragment;
import com.mantra.ionnews.ui.fragments.EditProfileFragment;
import com.mantra.ionnews.ui.fragments.HomeFragment;
import com.mantra.ionnews.ui.fragments.ProfileFragment;
import com.mantra.ionnews.ui.fragments.SearchFragment;
import com.mantra.ionnews.ui.fragments.SettingsFragment;
import com.mantra.ionnews.utils.LocalStorage;
import com.mantra.ionnews.utils.PicasoImageLoader;
import com.mantra.ionnews.utils.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Field;

import de.greenrobot.event.EventBus;

import static com.mantra.ionnews.utils.ConstantClass.DASHBOARD;
import static com.mantra.ionnews.utils.ConstantClass.EDIT_PROFILE;
import static com.mantra.ionnews.utils.ConstantClass.HOME;
import static com.mantra.ionnews.utils.ConstantClass.NEWDETAILS;
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

    public static BottomNavigationView bottomNavigationView;
    private ImageView imageViewCompanyLogo;
    BottomNavigationMenuView bottomNavigationMenuView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        registerEventBus();
        doubleBackToExitPressedOnce = false;

        initView();
        setUpViewPager();
        fetchStories();
        searchByTAG();
        disableShiftMode(bottomNavigationView);


        bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);;
        for (int i = 0; i < bottomNavigationMenuView.getChildCount(); i++) {
            final View iconView = bottomNavigationMenuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics);
            iconView.setLayoutParams(layoutParams);

        }



        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        Fragment fragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                fragment = new HomeFragment();
                                break;
                            case R.id.action_item2:
                               // fragment = new ProfileFragment();
                                openThisFragment(PROFILE);
                                break;
                            case R.id.action_item3:
                              //  fragment=new SearchFragment();
                                break;
                            case R.id.action_item4:
                                openThisFragment(EDIT_PROFILE);
                                break;
                            case R.id.action_item5:
                                openThisFragment(SETTINGS);
                                break;
                        }

                        if (fragment != null) {
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out);
                            fragmentTransaction.replace(R.id.ad_fragment_container, fragment);
                            fragmentTransaction.commitAllowingStateLoss();


                        } else {

                        }
                        return true;

                    }
                });
    }

    private void setUpViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.ad_view_pager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));


        imageViewCompanyLogo = (ImageView) findViewById(R.id.company_logo);
       /* PicasoImageLoader.with(DashboardActivity.this)
                .load(LocalStorage.getInstance(DashboardActivity.this).getCompanyImageUrl())
                .into(imageViewCompanyLogo);*/
        imageViewCompanyLogo.setImageResource(R.drawable.bottom_logo);
        imageViewCompanyLogo.setAdjustViewBounds(true);
    }

    protected void registerEventBus() {
        EventBus.getDefault().registerSticky(this);
    }

    public void onEvent(FragmentState fragmentState) {
        Log.d(TAG, fragmentState.getVisibleFragment());
        if (fragmentState.getVisibleFragment().equals(HOME)) {

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
        }
        else if (fragmentTag.equals(PROFILE)) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                    .replace(R.id.ad_fragment_container, ProfileFragment.newInstance(), PROFILE)
                    .addToBackStack(DASHBOARD)
                    .commit();
        }

        else {
            Log.e(TAG, "Fragment error!");
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "getBackStackEntryCount: " + getSupportFragmentManager().getBackStackEntryCount());
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            bottomNavigationView.getMenu().findItem(R.id.action_item1).setChecked(true);

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
    @SuppressLint("RestrictedApi")
    static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);

                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {

        } catch (IllegalAccessException e) {

        }
    }



        public void searchByTAG()
        {
            JSONObject jsonObject= new JSONObject();
            try
            {
                jsonObject.put("tags","#Arrow Internal Updates");
                jsonObject.put("user_id","95");
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            SearchByTagHandler searchByTagHandler = new SearchByTagHandler(this, this);
            if (Util.hasInternetAccess(this)) {
                searchByTagHandler.request(jsonObject);
                showProgressDialog(getString(R.string.loading));
            }

        }

}
