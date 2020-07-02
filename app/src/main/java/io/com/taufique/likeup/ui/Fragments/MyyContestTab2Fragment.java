package io.com.taufique.likeup.ui.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.com.taufique.likeup.ApiService;
import io.com.taufique.likeup.ApiServiceBuilder;
import io.com.taufique.likeup.Config;
import io.com.taufique.likeup.JsonModelObjects.ImageUploadResponse;
import io.com.taufique.likeup.JsonModelObjects.MyContest;
import io.com.taufique.likeup.Models.CustomItemClickListener;
import io.com.taufique.likeup.R;
import io.com.taufique.likeup.Singleton.MyContestSingleton;
import io.com.taufique.likeup.ui.adapter.MyContestAdapter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyyContestTab2Fragment extends Fragment {

    private List<MyContest> contestList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyContestAdapter contestAdapter;
    private SharedPreferences sharedPref;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FCR = 1;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int contestPosition;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyyContestTab2Fragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyyContestTab2Fragment newInstance(String param1, String param2) {
        MyyContestTab2Fragment fragment = new MyyContestTab2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_myy_contest_tab2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=getView().findViewById(R.id.rvMyContest);
        contestAdapter =new MyContestAdapter(MyContestSingleton.getInstance().getContests(),getActivity());
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (!MyContestSingleton.getInstance().isHasContest())
            syncContest();

        recyclerView.setAdapter(contestAdapter);
        contestAdapter.setCustomItemClickListener(new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                contestPosition =position;
                showImagePicker();


            }
        });




    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    private void syncContest(){
       // contestList.add(new Contest("........","......."));
        //contestList.add(new Contest(".........","........."));
        //contestList.add(new Contest("........","........"));

      //  ContestSingleton.getInstance().setContests(contestList);
        sharedPref = getActivity().getSharedPreferences("MY_KEY", Context.MODE_PRIVATE);
        Toast.makeText(getActivity(),"si"+sharedPref.getString("token",""),Toast.LENGTH_LONG).show();

        ApiService service= ApiServiceBuilder.getService();
        Call<List<MyContest>> call = service.getMyContests(sharedPref.getString("token",""));
        call.enqueue(new Callback<List<MyContest>>() {
            @Override
            public void onResponse(Call<List<MyContest>> call, Response<List<MyContest>> response) {

                Toast.makeText(getActivity(),"six",Toast.LENGTH_LONG).show();

                if(response.code()==200) {
                    MyContestSingleton.getInstance().setContests(response.body());
                    contestAdapter.setData(MyContestSingleton.getInstance().getContests());
                }
            }

            @Override
            public void onFailure(Call<List<MyContest>> call, Throwable t) {
                Toast.makeText(getActivity(),"sixehhhhh"+t.getMessage(),Toast.LENGTH_LONG).show();


            }
        });




    }




//////////////////////////////image upload/////////////////
private void showImagePicker() {
    if (file_permission()) {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        //checking for storage permission to write images for upload
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), perms, FCR);
            //checking for WRITE_EXTERNAL_STORAGE permission
        } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, FCR);
            //checking for CAMERA permissions
        } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, FCR);
        }
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent[] intentArray;
        intentArray = new Intent[]{pickPhoto};
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("*/*");
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Choose an action");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        startActivityForResult(chooserIntent, FCR);
    }
}

    public boolean file_permission() {
        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, FCR);
            return false;
        } else {
            return true;
        }
    }

    //creating new image file here
    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= 21) {
            Uri results = null;
            //checking if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FCR) {
                    results = data.getData();
                    handleSelectedImage(results);
                }
            }
            //////////////
        }
    }

    private void handleSelectedImage(Uri imageUri) {
        //////////////load the images on imageview
       // View header = navigationView.getHeaderView(0);
        //ImageView customer_avatar = header.findViewById(R.id.customer_avatar);
        //Toast.makeText(CustomerMainActivity.this, "image select " + getPath(imageUri), Toast.LENGTH_SHORT).show();
        File image = new File(getPath(imageUri));
       // Picasso.with(CustomerMainActivity.this).load(image).transform(new CircleTransform()).into(customer_avatar);
        uploadToServer(image);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private void uploadToServer(File file) {
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
        String accessToken = sharedPref.getString("token", "");
        RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"), accessToken);
        RequestBody contest_id = RequestBody.create(MediaType.parse("multipart/form-data"), ""+MyContestSingleton.getInstance().getContests().get(contestPosition).getId());

        ApiService service = ApiServiceBuilder.getService();
        Call<ImageUploadResponse> call = service.uploadImage(part, token, contest_id);
        call.enqueue(new Callback<ImageUploadResponse>() {
            @Override
            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                if (response.body() == null)
                    return;
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Image Upload" + response.body().getImgUrl(), Toast.LENGTH_SHORT).show();
                    ImageUploadResponse imageUploadResponse = response.body();
                    MyContestSingleton.getInstance().getContests().get(contestPosition).setImageUrl(Config.getFullUrl(imageUploadResponse.getImgUrl()));
                   // SharedPreferences.Editor editor = sharedPref.edit();

                }
            }

            @Override
            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                //Log.d("PostSnapResponse", t.getMessage());
            }
        });


    }




}
