package io.com.taufique.likeup.JsonModelObjects;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class MyContest {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("likes")
    @Expose
    private Integer likes;

    /**
     * No args constructor for use in serialization
     *
     */
    public MyContest() {
    }

    /**
     *
     * @param endDate
     * @param imageUrl
     * @param id
     * @param title
     * @param startDate
     * @param likes
     */
    public MyContest(Integer id, String imageUrl, String title, String startDate, String endDate, Integer likes) {
        super();
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.likes = likes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }


}
