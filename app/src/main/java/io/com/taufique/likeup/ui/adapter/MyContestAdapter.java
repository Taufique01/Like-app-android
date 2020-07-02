package io.com.taufique.likeup.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.com.taufique.likeup.Config;
import io.com.taufique.likeup.JsonModelObjects.MyContest;
import io.com.taufique.likeup.Models.CustomItemClickListener;
import io.com.taufique.likeup.R;

public class MyContestAdapter extends  RecyclerView.Adapter<MyContestAdapter.MyViewHolder>{



    private List<MyContest> contestList;

    Context mContext;
    CustomItemClickListener listener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,start_date,end_date,likes;

        public ImageView up_photo;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.my_contest_title);
            start_date = (TextView) view.findViewById(R.id.my_contest_start_date);
            end_date = (TextView) view.findViewById(R.id.my_contest_end_date);
            likes = (TextView) view.findViewById(R.id.my_contest_like);
            up_photo=view.findViewById(R.id.myContestImageView);


        }
    }

    public MyContestAdapter(List<MyContest> contestList,Context context) {
        this.contestList = contestList;
        this.mContext=context;

    }
    public void setData(List<MyContest> newData) {
        this.contestList.clear();
        this.contestList=newData;
        notifyDataSetChanged();
    }
    public void setCustomItemClickListener(CustomItemClickListener listener){



        this.listener=listener;
    }

    @NonNull
    @Override
    public MyContestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mycontest_list_row, parent, false);

        final MyContestAdapter.MyViewHolder mViewHolder = new MyContestAdapter.MyViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, mViewHolder.getAdapterPosition());
            }
        });
        return mViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyContestAdapter.MyViewHolder holder, int position) {
        MyContest contest = contestList.get(position);
        holder.title.setText(contest.getTitle());
        holder.start_date.setText(contest.getStartDate());
        holder.end_date.setText(contest.getEndDate());
        if(contest.getImageUrl()!=null) {
            Picasso.with(mContext).load(Config.url_base+contest.getImageUrl()).error(R.drawable.ic_priority_high_black_24dp).into(holder.up_photo);
            holder.likes.setText(contest.getLikes() + " likes");
        }
        //holder.entry_amount.setText("Entry: "+contest.getEntry_amount());
        //holder.prize_amount.setText("Prize: "+contest.getPrize_amount());
    }


    @Override
    public int getItemCount() {
        return contestList.size();
    }




}
