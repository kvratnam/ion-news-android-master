package com.mantra.ionnews.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikeStoryRequest {

    @SerializedName("content_id")
    @Expose
    private Integer contentId;

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

}