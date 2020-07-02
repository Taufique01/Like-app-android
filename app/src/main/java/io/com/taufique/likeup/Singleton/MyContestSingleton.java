package io.com.taufique.likeup.Singleton;

import java.util.ArrayList;
import java.util.List;

import io.com.taufique.likeup.JsonModelObjects.MyContest;
import io.com.taufique.likeup.Models.Contest;

public class MyContestSingleton {
    private static  MyContestSingleton instance=null;
    private List<MyContest> contests = new ArrayList<MyContest>();
    private boolean hasContest = false;

    private MyContestSingleton(){

    }


    public static MyContestSingleton getInstance(){


        if(instance==null)
            instance = new MyContestSingleton();

        return instance;

    }

    public List<MyContest> getContests() {

        return contests;
    }

    public void setContests(List<MyContest> contests) {
        hasContest=true;
        this.contests = contests;
    }

    public boolean isHasContest() {
        return hasContest;
    }

}
