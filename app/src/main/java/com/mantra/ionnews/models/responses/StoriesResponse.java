package com.mantra.ionnews.models.responses;

import com.mantra.ionnews.models.Story;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rajat on 30/03/17.
 */

public class StoriesResponse implements Serializable {

    String categoryTitle;
    List<Story> categoryStories;

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public List<Story> getCategoryStories() {
        return categoryStories;
    }

    public void setCategoryStories(List<Story> categoryStories) {
        this.categoryStories = categoryStories;
    }
}
