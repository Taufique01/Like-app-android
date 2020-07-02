package io.com.taufique.likeup.Singleton;

import java.util.ArrayList;
import java.util.List;

import io.com.taufique.likeup.JsonModelObjects.FeedItem;
import io.com.taufique.likeup.Models.Contest;

public class FeedSigleton {


    private static  FeedSigleton instance=null;
    private List<FeedItem> feedItems = new ArrayList<FeedItem>();
    private boolean hasFeed = false;

    private FeedSigleton(){

    }


    public static FeedSigleton getInstance(){


        if(instance==null)
            instance = new FeedSigleton();

        return instance;

    }

    public List<FeedItem> getFeedItems() {

        return feedItems;
    }

    public void setFeedItems(List<FeedItem> feedItems) {
        hasFeed=true;
        this.feedItems = feedItems;
    }

    public boolean isHasFeed() {
        return hasFeed;
    }

}
