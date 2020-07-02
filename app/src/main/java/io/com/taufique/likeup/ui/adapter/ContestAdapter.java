package io.com.taufique.likeup.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.com.taufique.likeup.Models.Contest;
import io.com.taufique.likeup.Models.CustomItemClickListener;
import io.com.taufique.likeup.R;

public class ContestAdapter extends  RecyclerView.Adapter<ContestAdapter.MyViewHolder> {

    private List<Contest> contestList;

    Context mContext;
    CustomItemClickListener listener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView entry_amount, prize_amount;
       // public ImageView dp_imageView;

        public MyViewHolder(View view) {
            super(view);
            entry_amount = (TextView) view.findViewById(R.id.entry_amount);
            prize_amount = (TextView) view.findViewById(R.id.prize_amount);


        }
    }

    public ContestAdapter(List<Contest> contestList) {
        this.contestList = contestList;

    }
    public void setData(List<Contest> newData) {
        this.contestList.clear();
        this.contestList=newData;
        notifyDataSetChanged();
    }
    public void setCustomItemClickListener(CustomItemClickListener listener){



        this.listener=listener;
    }

    @NonNull
    @Override
    public ContestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contest_list_row, parent, false);

        final MyViewHolder mViewHolder = new MyViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, mViewHolder.getAdapterPosition());
            }
        });
        return mViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ContestAdapter.MyViewHolder holder, int position) {
        Contest contest = contestList.get(position);

        holder.entry_amount.setText("Entry: "+contest.getEntry_amount());
        holder.prize_amount.setText("Prize: "+contest.getPrize_amount());
    }

    @Override
    public int getItemCount() {
        return contestList.size();
    }
}

