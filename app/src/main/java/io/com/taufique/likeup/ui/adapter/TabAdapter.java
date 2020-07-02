package io.com.taufique.likeup.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import io.com.taufique.likeup.ui.Fragments.ContestsTab1Fragment;
import io.com.taufique.likeup.ui.Fragments.MyyContestTab2Fragment;

public class TabAdapter extends FragmentStateAdapter {
   private final int TAB_ITEM_COUNT=2;

    public TabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(position==0)
            return new ContestsTab1Fragment();
        //else if(position==1)
        return new MyyContestTab2Fragment();



    }

    @Override
    public int getItemCount() {
        return TAB_ITEM_COUNT;
    }
}