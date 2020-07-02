package io.com.taufique.likeup.JsonModelObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedItem {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("username")
    @Expose
    private String userName;

    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("avatarUrl")
    @Expose
    private String avatarUrl;

    @SerializedName("isLiked")
    @Expose
    private boolean isliked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isIsliked() {
        return isliked;
    }

    public void setIsliked(boolean isliked) {
        this.isliked = isliked;
    }


    public boolean likeClicked(){


        if(isliked) {
            this.likes = this.likes - 1;
            this.isliked=false;
            return this.isliked;
        }

        this.likes=this.likes+1;
        this.isliked=true;
        return this.isliked;



    }

}
