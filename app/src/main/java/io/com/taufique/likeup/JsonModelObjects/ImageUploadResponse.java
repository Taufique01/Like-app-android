package io.com.taufique.likeup.JsonModelObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ImageUploadResponse
{
    @Expose
    @SerializedName("img_url")
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }
}
