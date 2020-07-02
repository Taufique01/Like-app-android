package io.com.taufique.likeup.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contest {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("entry")
    @Expose
    private String entry_amount;
    @SerializedName("prize")
    @Expose
    private String prize_amount;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("start_date_str")
    @Expose
    private String start_date_str;
    @SerializedName("end_date_str")
    @Expose
    private  String end_date_str;

    @SerializedName("max_participant")
    @Expose
    private  String max_participant;

    public Contest(String entry, String prize){

        this.prize_amount="Entry: "+entry;
        this.entry_amount="Prize: "+prize;
    }

    public String getEntry_amount() {
        return entry_amount;
    }

    public void setEntry_amount(String entry_amount) {
        this.entry_amount = entry_amount;
    }

    public String getPrize_amount() {
        return prize_amount;
    }

    public void setPrize_amount(String prize_amount) {
        this.prize_amount = prize_amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart_date_str() {
        return start_date_str;
    }

    public void setStart_date_str(String start_date_str) {
        this.start_date_str = start_date_str;
    }

    public String getEnd_date_str() {
        return end_date_str;
    }

    public void setEnd_date_str(String end_date_str) {
        this.end_date_str = end_date_str;
    }

    public String getMax_participant() {
        return max_participant;
    }

    public void setMax_participant(String max_participant) {
        this.max_participant = max_participant;
    }

    public Integer getId() {
        return id;
    }
}
