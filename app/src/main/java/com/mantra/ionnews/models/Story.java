package com.mantra.ionnews.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Story implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("sub_title")
    @Expose
    private String subTitle;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("crawl_url")
    @Expose
    private String crawlUrl;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("all_users")
    @Expose
    private String allUsers;
    @SerializedName("all_category")
    @Expose
    private String allCategory;
    @SerializedName("expiry_date")
    @Expose
    private String expiryDate;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("like_count")
    @Expose
    private Integer likeCount;
    @SerializedName("islike")
    @Expose
    private Integer islike;
    @SerializedName("custom_url")
    @Expose
    private String customUrl;
    @SerializedName("custom_content_id")
    @Expose
    private Integer customContentId;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCrawlUrl() {
        return crawlUrl;
    }

    public void setCrawlUrl(String crawlUrl) {
        this.crawlUrl = crawlUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(String allUsers) {
        this.allUsers = allUsers;
    }

    public String getAllCategory() {
        return allCategory;
    }

    public void setAllCategory(String allCategory) {
        this.allCategory = allCategory;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getIslike() {
        return islike;
    }

    public void setIslike(Integer islike) {
        this.islike = islike;
    }

    public String getCustomUrl() {
        return customUrl;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }

    public Integer getCustomContentId() {
        return customContentId;
    }

    public void setCustomContentId(Integer customContentId) {
        this.customContentId = customContentId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

}