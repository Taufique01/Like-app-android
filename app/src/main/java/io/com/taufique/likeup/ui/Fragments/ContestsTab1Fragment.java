package io.com.taufique.likeup.ui.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.com.taufique.likeup.ApiService;
import io.com.taufique.likeup.ApiServiceBuilder;
import io.com.taufique.likeup.Models.Contest;
import io.com.taufique.likeup.Models.CustomItemClickListener;
import io.com.taufique.likeup.R;
import io.com.taufique.likeup.Singleton.ContestSingleton;
import io.com.taufique.likeup.ui.activity.ContestActivity;
import io.com.taufique.likeup.ui.activity.PaymentActivity;
import io.com.taufique.likeup.ui.adapter.ContestAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContestsTab1Fragment extends Fragment {

    private List<Contest> contestList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContestAdapter contestAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ContestsTab1Fragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ContestsTab1Fragment newInstance(String param1, String param2) {
        ContestsTab1Fragment fragment = new ContestsTab1Fragment();
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
        return inflater.inflate(R.layout.fragment_contests_tab1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=getView().findViewById(R.id.rvContest);
        contestAdapter =new ContestAdapter(ContestSingleton.getInstance().getContests());
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (!ContestSingleton.getInstance().isHasContest())
            syncContest();
        recyclerView.setAdapter(contestAdapter);
        contestAdapter.setCustomItemClickListener(new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {


                Intent intent=new Intent(getActivity(), PaymentActivity.class);
                intent.putExtra("contest_id",Integer.toString(ContestSingleton.getInstance().getContests().get(position).getId()));
                intent.putExtra("amount",ContestSingleton.getInstance().getContests().get(position).getEntry_amount());

                startActivity(intent);
               // ContestSingleton.getInstance().getContests().add(new Contest("2000","2000"));
                //syncContest();
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
        contestList.add(new Contest("........","......."));
        contestList.add(new Contest(".........","........."));
        contestList.add(new Contest("........","........"));

        ContestSingleton.getInstance().setContests(contestList);

        ApiService service= ApiServiceBuilder.getService();
        Call<List<Contest>> call = service.getContests();
        call.enqueue(new Callback<List<Contest>>() {
            @Override
            public void onResponse(Call<List<Contest>> call, Response<List<Contest>> response) {

                Toast.makeText(getActivity(),"six",Toast.LENGTH_LONG).show();

                ContestSingleton.getInstance().setContests(response.body());
                contestAdapter.setData(ContestSingleton.getInstance().getContests());
            }

            @Override
            public void onFailure(Call<List<Contest>> call, Throwable t) {
                Toast.makeText(getActivity(),"sixehhhhh"+t.getMessage(),Toast.LENGTH_LONG).show();


            }
        });




    }
}
