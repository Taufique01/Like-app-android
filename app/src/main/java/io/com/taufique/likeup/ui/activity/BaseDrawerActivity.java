package io.com.taufique.likeup.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.BindDimen;
import butterknife.BindString;
import io.com.taufique.likeup.ApiService;
import io.com.taufique.likeup.ApiServiceBuilder;
import io.com.taufique.likeup.R;
import io.com.taufique.likeup.ui.utils.CircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Miroslaw Stanek on 15.07.15.
 */
public class BaseDrawerActivity extends BaseActivity {

    private SharedPreferences sharedPref;
   // private GoogleSignInClient mGoogleSignInClient;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.vNavigation)
    NavigationView vNavigation;

    @BindDimen(R.dimen.global_menu_avatar_size)
    int avatarSize;
    @BindString(R.string.user_profile_photo)
    String profilePhoto;

    //Cannot be bound via Butterknife, hosting view is initialized later (see setupHeader() method)
    private ImageView ivMenuUserProfilePhoto;

    private TextView ivMenuUsername;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentViewWithoutInject(R.layout.activity_drawer);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.flContentRoot);
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);
        bindViews();
        setupHeader();
        setupItems();
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (getToolbar() != null) {
            getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            });
        }
    }

    private void setupHeader() {
        View headerView = vNavigation.getHeaderView(0);
        ivMenuUserProfilePhoto = (ImageView) headerView.findViewById(R.id.ivMenuUserProfilePhoto);

        ivMenuUsername=headerView.findViewById(R.id.ivMenuUsername);
        headerView.findViewById(R.id.vGlobalMenuHeader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGlobalMenuHeaderClick(v);
            }
        });


        sharedPref = getSharedPreferences("MY_KEY", Context.MODE_PRIVATE);
                  ivMenuUsername.setText(sharedPref.getString("name", "You are Logged in"));

        Picasso.with(this)
                .load(sharedPref.getString("avatar","https://imgur.com/a/IhETAND"))
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .into(ivMenuUserProfilePhoto);
    }

    public void onGlobalMenuHeaderClick(final View v) {
        drawerLayout.closeDrawer(Gravity.LEFT);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] startingLocation = new int[2];
                v.getLocationOnScreen(startingLocation);
                startingLocation[0] += v.getWidth() / 2;
                UserProfileActivity.startUserProfileFromLocation(startingLocation, BaseDrawerActivity.this);
                overridePendingTransition(0, 0);
            }
        }, 200);
    }


    private void setupItems(){

        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch(id)
                {
                    case R.id.menu_feed:

                        Toast.makeText(BaseDrawerActivity.this, "my feed",Toast.LENGTH_SHORT).show();
                        openFeedActivity();
                        break;
                    case R.id.menu_competitons:
                        Intent intent = new Intent(BaseDrawerActivity.this,ContestActivity.class);
                        startActivity(intent);

                        Toast.makeText(BaseDrawerActivity.this, "Settings",Toast.LENGTH_SHORT).show();break;
                    case R.id.menu_earnings:
                        Toast.makeText(BaseDrawerActivity.this, "My earnings",Toast.LENGTH_SHORT).show();break;
                    case R.id.menu_profile:
                        Toast.makeText(BaseDrawerActivity.this, "My profile",Toast.LENGTH_SHORT).show();
                    case R.id.menu_terms:
                        Toast.makeText(BaseDrawerActivity.this, "My terms",Toast.LENGTH_SHORT).show();
                    case R.id.menu_logout:
                        signOut();
                        Toast.makeText(BaseDrawerActivity.this, "My logout",Toast.LENGTH_SHORT).show();

                        default:
                        return true;
                }


                return true;

            }

        });



    }


    private void signOut() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        sharedPref = getSharedPreferences("MY_KEY", Context.MODE_PRIVATE);

                        logoutToServer(sharedPref.getString("token", ""));
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.remove("token");

                    }
                });
    }


    private void logoutToServer(final String token) {
       // Log.d(TAG, "logoutToServer: inside logoutToServer " + token);
        ApiService service = ApiServiceBuilder.getService();
        Call<Void> call = service.getToken(token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    private void openFeedActivity(){



        Intent intent =new Intent(BaseDrawerActivity.this,MainActivity.class);
        startActivity(intent);
    }


}
