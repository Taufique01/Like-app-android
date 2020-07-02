package io.com.taufique.likeup.ui.activity;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import io.com.taufique.likeup.ApiService;
import io.com.taufique.likeup.ApiServiceBuilder;
import io.com.taufique.likeup.JsonModelObjects.FeedItem;
import io.com.taufique.likeup.JsonModelObjects.LikedResponse;
import io.com.taufique.likeup.Models.Contest;
import io.com.taufique.likeup.R;
import io.com.taufique.likeup.Singleton.ContestSingleton;
import io.com.taufique.likeup.Singleton.FeedSigleton;
import io.com.taufique.likeup.Utils;
import io.com.taufique.likeup.ui.adapter.FeedAdapter;
import io.com.taufique.likeup.ui.adapter.FeedItemAnimator;
import io.com.taufique.likeup.ui.utils.CircleTransformation;
import io.com.taufique.likeup.ui.view.FeedContextMenu;
import io.com.taufique.likeup.ui.view.FeedContextMenuManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseDrawerActivity implements FeedAdapter.OnFeedItemClickListener,
        FeedContextMenu.OnFeedContextMenuItemClickListener {
    public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";

    public static final String GOOGLE_ACCOUNT = "google_account";

    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;
    private SharedPreferences sharedPref;

    private List<FeedItem> feedItems=new ArrayList<FeedItem>();
    @BindView(R.id.rvFeed)
    RecyclerView rvFeed;
   // @BindView(R.id.btnCreate)
    //FloatingActionButton fabCreate;
    @BindView(R.id.content)
   CoordinatorLayout clContent;

    private FeedAdapter feedAdapter;

    private boolean pendingIntroAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = getSharedPreferences("MY_KEY", Context.MODE_PRIVATE);


        setupFeed();

       // if (savedInstanceState == null)
         //   pendingIntroAnimation = true;
        if(!FeedSigleton.getInstance().isHasFeed())
            syncFeedData();




       // } else {



        //}
    }

    private void setupFeed() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        rvFeed.setLayoutManager(linearLayoutManager);

        feedAdapter = new FeedAdapter(this,FeedSigleton.getInstance().getFeedItems()){


            @Override
            protected boolean onLikeCButtonClicked(FeedItem feedItem,final int position) {

                ApiService service= ApiServiceBuilder.getService();
                String accessToken = sharedPref.getString("token", "");

                Call<LikedResponse> call = service.getFeedItems(accessToken,feedItem.getId(),feedItem.isIsliked());
                call.enqueue(new Callback<LikedResponse>() {
                    @Override
                    public void onResponse(Call<LikedResponse> call, Response<LikedResponse> response) {

                        if(response.code()!=200)
                            return ;
                        // Toast.makeText(MainActivity.this, "Feed Retrieving Failure! " + t.getMessage(), Toast.LENGTH_LONG).show();

                        FeedSigleton.getInstance().getFeedItems().get(position).setIsliked(response.body().isLiked());

                    }

                    @Override
                    public void onFailure(Call<LikedResponse> call, Throwable t) {
                       // Toast.makeText(MainActivity.this, "Feed Retrieving Failure! " + t.getMessage(), Toast.LENGTH_LONG).show();


                    }
            });

                return FeedSigleton.getInstance().getFeedItems().get(position).isIsliked();
        }
        };


        feedAdapter.setOnFeedItemClickListener(this);
        rvFeed.setAdapter(feedAdapter);
        rvFeed.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                FeedContextMenuManager.getInstance().onScrolled(recyclerView, dx, dy);
            }
        });
        rvFeed.setItemAnimator(new FeedItemAnimator());
    }

    private void syncFeedData(){



        ApiService service= ApiServiceBuilder.getService();
        String accessToken = sharedPref.getString("token", "");

        Call<List<FeedItem>> call = service.getFeedItems(accessToken);
        call.enqueue(new Callback<List<FeedItem>>() {
            @Override
            public void onResponse(Call<List<FeedItem>> call, Response<List<FeedItem>> response) {

                if(response.code()!=200)
                    return;
                Toast.makeText(MainActivity.this,"six",Toast.LENGTH_LONG).show();

                FeedSigleton.getInstance().setFeedItems(response.body());
            //    if (pendingIntroAnimation) {
              //      pendingIntroAnimation = false;
                    startIntroAnimation();
                   // return;

                //}
                //feedAdapter.updateItems(false, FeedSigleton.getInstance().getFeedItems());
            }

            @Override
            public void onFailure(Call<List<FeedItem>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Feed Retrieving Failure! "+t.getMessage(),Toast.LENGTH_LONG).show();


            }
        });

















        }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (ACTION_SHOW_LOADING_ITEM.equals(intent.getAction())) {
            showFeedLoadingItemDelayed();
        }
    }

    private void showFeedLoadingItemDelayed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rvFeed.smoothScrollToPosition(0);
                feedAdapter.showLoadingView();
            }
        }, 500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        return true;
    }

    private void startIntroAnimation() {
        //fabCreate.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));

        int actionbarSize = Utils.dpToPx(56);
        getToolbar().setTranslationY(-actionbarSize);
        getIvLogo().setTranslationY(-actionbarSize);
        getInboxMenuItem().getActionView().setTranslationY(-actionbarSize);

        getToolbar().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(300);
        getIvLogo().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(400);
        getInboxMenuItem().getActionView().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startContentAnimation();
                    }
                })
                .start();
    }

    private void startContentAnimation() {

        feedAdapter.updateItems(true,FeedSigleton.getInstance().getFeedItems());
    }

    @Override
    public void onCommentsClick(View v, int position) {
       // final Intent intent = new Intent(this, CommentsActivity.class);
        //int[] startingLocation = new int[2];
        //v.getLocationOnScreen(startingLocation);
        //intent.putExtra(CommentsActivity.ARG_DRAWING_START_LOCATION, startingLocation[1]);
        //startActivity(intent);
        //overridePendingTransition(0, 0);
    }

    @Override
    public void onMoreClick(View v, int itemPosition) {
        FeedContextMenuManager.getInstance().toggleContextMenuFromView(v, itemPosition, this);
    }

    @Override
    public void onProfileClick(View v) {
      //  int[] startingLocation = new int[2];
       // v.getLocationOnScreen(startingLocation);
        //startingLocation[0] += v.getWidth() / 2;
        //UserProfileActivity.startUserProfileFromLocation(startingLocation, this);
        //overridePendingTransition(0, 0);
    }

    @Override
    public void onReportClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onSharePhotoClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onCopyShareUrlClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onCancelClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    //@OnClick(R.id.btnCreate)
    //public void onTakePhotoClick() {
        //int[] startingLocation = new int[2];
      //  fabCreate.getLocationOnScreen(startingLocation);
        //startingLocation[0] += fabCreate.getWidth() / 2;
        //TakePhotoActivity.startCameraFromLocation(startingLocation, this);
        //overridePendingTransition(0, 0);
    //}

    public void showLikedSnackbar() {
        Snackbar.make(clContent, "Liked!", Snackbar.LENGTH_SHORT).show();
    }

   // private void setDataOnView() {
     //   GoogleSignInAccount googleSignInAccount = getIntent().getParcelableExtra(GOOGLE_ACCOUNT);
       // ImageView   profilePhoto = (ImageView) headerView.findViewById(R.id.ivMenuUserProfilePhoto);

        //Picasso.with(this)
          //      .load(googleSignInAccount.getPhotoUrl())
            //    .placeholder(R.drawable.img_circle_placeholder)
              //  .resize(avatarSize, avatarSize)
                //.centerCrop()
                //.transform(new CircleTransformation())
                //.into(ivMenuUserProfilePhoto);

       // Picasso.get().load(googleSignInAccount.getPhotoUrl()).centerInside().fit().into(profileImage);
        //profileName.setText(googleSignInAccount.getDisplayName());
        //profileEmail.setText(googleSignInAccount.getEmail());
    //}

}