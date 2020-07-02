package io.com.taufique.likeup.JsonModelObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikedResponse {

    @SerializedName("isLiked")
    @Expose
    private boolean liked;

    public boolean isLiked() {
        return liked;
    }
}
