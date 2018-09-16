package com.mantra.ionnews.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mantra.ionnews.models.Story;

import java.util.List;

public class Data {

    @SerializedName("all_data")
    @Expose
    private List<Story> allData = null;

    public List<Story> getAllData() {
        return allData;
    }

    public void setAllData(List<Story> allData) {
        this.allData = allData;
    }

}