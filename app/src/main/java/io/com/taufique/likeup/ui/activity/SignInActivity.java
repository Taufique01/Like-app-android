package io.com.taufique.likeup.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import io.com.taufique.likeup.ApiService;
import io.com.taufique.likeup.ApiServiceBuilder;
import io.com.taufique.likeup.JsonModelObjects.ResponseBodyLogin;
import io.com.taufique.likeup.R;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SignInActivity extends AppCompatActivity {


    private SharedPreferences sharedPref;

    private GoogleSignInClient mGoogleSignInClient;
    final int RC_SIGN_IN = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);
        sharedPref = getSharedPreferences("MY_KEY", Context.MODE_PRIVATE);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               .requestIdToken(getString(R.string.server_client_id))

                

                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient.silentSignIn()
                .addOnCompleteListener(
                        this,
                        new OnCompleteListener<GoogleSignInAccount>() {
                            @Override
                            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                                handleSignInResult(task);
                            }
                        });

       // Check for existing Google Sign In account, if the user is already signed in
       // the GoogleSignInAccount will be non-null.f


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
       // showToast("Sign in error! Please,");

        if (account == null) {
            showToast("Sign in error! updateui");
            return;
        }
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);


        startActivity(intent);
    }

    private void updatePrivateData(GoogleSignInAccount account) {
        showToast("Sign in");

        SharedPreferences.Editor editor = sharedPref.edit();
        try {
            editor.putString("name",account.getDisplayName());
            editor.putString("email", account.getEmail());
            editor.putString("avatar", account.getPhotoUrl().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.commit();



    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getServerAuthCode();//account.getIdToken();
            showToast("idtoken"+idToken);
            // Signed in successfully, show authenticated UI.
            loginToServer(account);

        } catch (ApiException e) {
            showToast("exp"+e.getMessage()+e.getStatusCode());
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            // Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }




    private void loginToServer(final GoogleSignInAccount account) {

        ApiService service = ApiServiceBuilder.getService();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("client_id", getString(R.string.CLIENT_ID));
            jsonObject.put("client_secret", getString(R.string.CLIENT_SECRET));
            jsonObject.put("token", account.getIdToken());
           // Log.d(TAG, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        Call<ResponseBodyLogin> call = service.facebookLogin(requestBody);
        call.enqueue(new Callback<ResponseBodyLogin>() {
            @Override
            public void onResponse(Call<ResponseBodyLogin> call, Response<ResponseBodyLogin> response) {
                //Log.d(TAG, response.toString());
                ResponseBodyLogin responseBodyLogin = response.body();
                showToast(responseBodyLogin.getAccessToken());
              //  Log.d(TAG, "onResponse: response.body() " + responseBodyLogin);
                SharedPreferences.Editor editor = sharedPref.edit();
                try {
                    editor.putString("token", responseBodyLogin.getAccessToken());
                   // editor.putString("login_method", "facebook");
                   // Log.d(TAG, "onResponse: accesstoken from response body login " + responseBodyLogin.getAccessToken());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                editor.commit();
                updatePrivateData(account);
                updateUI(account);

            }

            @Override
            public void onFailure(Call<ResponseBodyLogin> call, Throwable t) {
                Toast.makeText(SignInActivity.this, "Sorry! Somme Error Occured" + t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();


    }
}
