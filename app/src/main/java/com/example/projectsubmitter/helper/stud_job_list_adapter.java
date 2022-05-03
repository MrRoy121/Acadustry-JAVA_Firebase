package com.example.projectsubmitter.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.projectsubmitter.R;
import com.example.projectsubmitter.classroom.cls_room;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class stud_job_list_adapter extends RecyclerView.Adapter<stud_job_list_adapter.ViewHolder>{
    private stud_job_list[] listdata;
    private Context context;

    public stud_job_list_adapter(List<stud_job_list> listdata, Context context) {
        this.listdata = listdata.toArray(new stud_job_list[0]);
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.stud_room_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final stud_job_list myListData = listdata[position];
        holder.textView.setText(listdata[position].getCode());
        FirebaseFirestore.getInstance().collection("Rooms").whereEqualTo("random", myListData.getCode()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot qs) {
                for(DocumentSnapshot ds : qs){
                    holder.t.setText(ds.getString("teacherName"));
                    holder.sub.setText(ds.getString("subject"));
                    holder.sec.setText(ds.getString("section"));
                }
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, cls_room.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("stud", true);
                bundle.putString("Name", myListData.getName());
                bundle.putString("ran", myListData.getCode());
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
        public TextView textView, t, sec, sub;
        public CardView relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.sec = (TextView) itemView.findViewById(R.id.sec);
            this.sub = (TextView) itemView.findViewById(R.id.subject);
            this.t = (TextView) itemView.findViewById(R.id.pcname);
            this.textView = (TextView) itemView.findViewById(R.id.pitle);
            this.relativeLayout = (CardView) itemView.findViewById(R.id.layout);
        }
    }
}