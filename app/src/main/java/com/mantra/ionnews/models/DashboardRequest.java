package com.mantra.ionnews.models;

/**
 * Created by TaNMay on 28/09/16.
 */
public class DashboardRequest {

    String storiesResponse;

    public DashboardRequest(String storiesResponse) {
        this.storiesResponse = storiesResponse;
    }

    public String getStoriesResponse() {
        return storiesResponse;
    }
}
