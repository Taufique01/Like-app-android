package io.com.taufique.likeup.JsonModelObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContestHistory {



    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("prize")
    @Expose
    private String prize_amount;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("end_date_str")
    @Expose
    private  String end_date_str;

    @SerializedName("winner")
    @Expose
    private  Winner winner;

    public Integer getId() {
        return id;
    }

    public String getPrize_amount() {
        return prize_amount;
    }

    public String getTitle() {
        return title;
    }

    public String getEnd_date_str() {
        return end_date_str;
    }

    public Winner getWinner() {
        return winner;
    }


}
