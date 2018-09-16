package com.mantra.ionnews.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mantra.ionnews.ui.fragments.NewsFragment;
import com.mantra.ionnews.ui.fragments.ProfileFragment;

/**
 * Created by TaNMay on 30/03/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        switch (pos) {
            case 0:
                return ProfileFragment.newInstance();
            case 1:
                return NewsFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
