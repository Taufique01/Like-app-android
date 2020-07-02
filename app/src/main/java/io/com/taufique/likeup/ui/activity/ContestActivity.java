package io.com.taufique.likeup.ui.activity;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

import io.com.taufique.likeup.R;
import io.com.taufique.likeup.ui.Fragments.ContestsTab1Fragment;
import io.com.taufique.likeup.ui.Fragments.MyyContestTab2Fragment;
import io.com.taufique.likeup.ui.adapter.TabAdapter;

public class ContestActivity extends BaseDrawerActivity implements ContestsTab1Fragment.OnFragmentInteractionListener,MyyContestTab2Fragment.OnFragmentInteractionListener {


    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private final List<String> TAB_TITLE = Arrays.asList("Contests", "My Contests");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);

          viewPager = findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager(), new Lifecycle() {
            @Override
            public void addObserver(@NonNull LifecycleObserver observer) {

            }

            @Override
            public void removeObserver(@NonNull LifecycleObserver observer) {

            }

            @NonNull
            @Override
            public State getCurrentState() {
                return State.RESUMED;
            }
        });



       viewPager.setAdapter(adapter);
        //tabLayout.setupWithViewPager(viewPager);


        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(TAB_TITLE.get(position));
                    }
                }).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
