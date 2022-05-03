package com.example.projectsubmitter.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.projectsubmitter.R;

import java.util.ArrayList;

public class message_adapter extends RecyclerView.Adapter<message_adapter.ViewHolder>{
    private post_helper[] listdata;
    private Context context;

    // RecyclerView recyclerView;
    public message_adapter(ArrayList<post_helper> listdata, Context context) {
        this.listdata = listdata.toArray(new post_helper[0]);
        this.context = context;
    }
    @Override
    public  message_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.message_list, parent, false);
        message_adapter.ViewHolder viewHolder = new message_adapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(message_adapter.ViewHolder holder, int position) {
        final post_helper myListData = listdata[position];
        holder.textView.setText(listdata[position].getUsername());
        holder.textView1.setText(listdata[position].getTitle());
        holder.textView2.setText(listdata[position].getUserID());

        }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView,textView1,textView2;
        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.sender);
            this.textView1 = (TextView) itemView.findViewById(R.id.mess_time);
            this.textView2 = (TextView) itemView.findViewById(R.id.message);
        }
    }
}