package com.mantra.ionnews.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.mantra.ionnews.R;
import com.mantra.ionnews.adapters.ExpandableListAdapter;
import com.mantra.ionnews.models.responses.StoriesResponse;
import com.mantra.ionnews.utils.LocalStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpendableListFragment extends Fragment {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListHeader;
    String titleStr = null;
    HashMap<String, List<String>> expandableListDetail;
    private List<StoriesResponse> storiesResponseList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

      View view= inflater.inflate(R.layout.expand_list_activity,null,false);
        expandableListView = (ExpandableListView)view.findViewById(R.id.expandableListView);
        //Static
      //  expandableListDetail = getData();

        //dynamic

        expandableListHeader = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new ExpandableListAdapter(getActivity(), expandableListHeader, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                parent.smoothScrollToPosition(groupPosition);

                if (parent.isGroupExpanded(groupPosition)) {
                    ImageView imageView = v.findViewById(R.id.expandable_icon1);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
                } else {
                    ImageView imageView = v.findViewById(R.id.expandable_icon1);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
                }

                //  showToastMessage(expandableListHeader.get(groupPosition));

                return false    ;
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

                //   showToastMessage(expandableListHeader.get(groupPosition));

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                //   showToastMessage(expandableListDetail.get(expandableListHeader.get(groupPosition)).get(childPosition));
                return false;
            }
        });
        return view;
    }

    public void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        for(int i =0 ;i<storiesResponseList.size();i++)
        {
            titleStr = storiesResponseList.get(i).getCategoryTitle();
            expandableListDetail.get(titleStr);
        }

       /*

        List<String> quarterBack = new ArrayList<String>();
        quarterBack.add(" Kevin Burke");
        quarterBack.add("Dylan Favre");

        List<String> offensiveTeam = new ArrayList<String>();
        offensiveTeam.add("James Atoe");
        offensiveTeam.add("Mike Criste");
        offensiveTeam.add("Jeremy Galten");
        offensiveTeam.add("Randall Harris");
        offensiveTeam.add("Alex Land");

        List<String> defensive = new ArrayList<String>();
        defensive.add("Calvin Burnett Jr.");
        defensive.add("Bryan Douglas");
        defensive.add("Lucky Dozier");
        defensive.add("Curtis Slater");
        defensive.add("Cliff Stokes");

        List<String> linebackers = new ArrayList<String>();
        linebackers.add("B. J. Beatty");
        linebackers.add("David Guthrie");
        linebackers.add("Matt Oh");
        linebackers.add("Scott Thompson");
        linebackers.add("Talib Wise");

        expandableListDetail.put("QUATERBACK TEAM", quarterBack);
        expandableListDetail.put("OFFENSIVE TEAM", offensiveTeam);
        expandableListDetail.put("DEFENSIVE", defensive);
        expandableListDetail.put("LINEBACKERS", linebackers);*/

        return expandableListDetail;
    }
}