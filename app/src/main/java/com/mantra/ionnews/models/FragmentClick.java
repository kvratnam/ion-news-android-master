package com.mantra.ionnews.models;

/**
 * Created by TaNMay on 28/09/16.
 */
public class FragmentClick {

    private String viewClicked;
    private String tag;

    public FragmentClick(String viewClicked, String tag) {
        this.viewClicked = viewClicked;
        this.tag = tag;
    }

    public String getViewClicked() {
        return viewClicked;
    }

    public String getTag() {
        return tag;
    }
}
