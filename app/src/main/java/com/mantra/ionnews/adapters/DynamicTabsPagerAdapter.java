package com.mantra.ionnews.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mantra.ionnews.R;
import com.mantra.ionnews.models.responses.StoriesResponse;
import com.mantra.ionnews.ui.fragments.StoriesFragment;

import java.util.List;

/**
 * Created by rajat on 29/03/17.
 */

public class DynamicTabsPagerAdapter extends FragmentStatePagerAdapter {

    private List<StoriesResponse> storiesResponses;
    private Context context;

    public DynamicTabsPagerAdapter(FragmentManager fragmentManager, List<StoriesResponse> storiesResponses, Context context) {
        super(fragmentManager);
        this.storiesResponses = storiesResponses;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        StoriesFragment storiesFragment = StoriesFragment.newInstance(storiesResponses.get(position));
        return storiesFragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return storiesResponses.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == getCount() - 1) {
            return storiesResponses.get(position).getCategoryTitle()
                    + "                                        ";
        } else if (position == 0) {
            return "  " + storiesResponses.get(position).getCategoryTitle();
        }
        return storiesResponses.get(position).getCategoryTitle();
    }

    public View getTabView(int position) {
        View tabIcon = LayoutInflater.from(context).inflate(R.layout.custom_tab_icon, null);
        TextView tabTitle = (TextView) tabIcon.findViewById(R.id.cti_title);
        tabTitle.setText(storiesResponses.get(position).getCategoryTitle());
        return tabIcon;
    }
}
