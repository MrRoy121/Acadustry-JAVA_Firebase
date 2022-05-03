package com.example.projectsubmitter.submission;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectsubmitter.R;
import com.example.projectsubmitter.classroom.cls_room;

import java.util.ArrayList;

public class prob_submission_adapter extends RecyclerView.Adapter<prob_submission_adapter.ViewHolder>{
    private my_submission_list[] listdata;
    private Context context;

    // RecyclerView recyclerView;
    public prob_submission_adapter(ArrayList<my_submission_list> listdata, Context context) {
        this.listdata = listdata.toArray(new my_submission_list[0]);
        this.context = context;
    }
    @Override
    public prob_submission_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.file_list, parent, false);
       prob_submission_adapter.ViewHolder viewHolder = new prob_submission_adapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(prob_submission_adapter.ViewHolder holder, int position) {
        final my_submission_list myListData = listdata[position];
        holder.name.setText(listdata[position].getTitle());
        holder.i.setImageResource(R.drawable.menu);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, submission2.class);
                Bundle bundle = new Bundle();
                bundle.putString("ran", myListData.getCode());
                bundle.putString("title", myListData.getTitle());
                i.putExtras(bundle);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView i;
        public CardView relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.upload_filename);
            this.i = (ImageView) itemView.findViewById(R.id.upload_icon);
            this.relativeLayout = (CardView) itemView.findViewById(R.id.cview);
        }
    }
}