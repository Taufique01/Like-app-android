package io.com.taufique.likeup.Singleton;

import java.util.ArrayList;
import java.util.List;

import io.com.taufique.likeup.Models.Contest;

public final class ContestSingleton {
    private static  ContestSingleton instance=null;
    private List<Contest> contests = new ArrayList<Contest>();
   private boolean hasContest = false;

    private ContestSingleton(){

    }


    public static ContestSingleton getInstance(){


        if(instance==null)
            instance = new ContestSingleton();

        return instance;

    }

    public List<Contest> getContests() {

        return contests;
    }

    public void setContests(List<Contest> contests) {
        hasContest=true;
        this.contests = contests;
    }

    public boolean isHasContest() {
        return hasContest;
    }
}
