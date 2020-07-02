package io.com.taufique.likeup.JsonModelObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseBodyLogin {



    @SerializedName("access_token")
    @Expose
    private String accessToken;


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "ResponseBodyLogin{" +

                "accessToken='" + accessToken +  '}';
    }
}
