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

public class room_student_list_adapter extends RecyclerView.Adapter<room_student_list_adapter.ViewHolder>{
    private room_stud_list[] listdata;
    private Context context;

    // RecyclerView recyclerView;
    public room_student_list_adapter(ArrayList<room_stud_list> listdata, Context context) {
        this.listdata = listdata.toArray(new room_stud_list[0]);
        this.context = context;
    }
    @Override
    public room_student_list_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.room_stud_list, parent, false);
        room_student_list_adapter.ViewHolder viewHolder = new room_student_list_adapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(room_student_list_adapter.ViewHolder holder, int position) {
        final room_stud_list myListData = listdata[position];
        holder.textView.setText(listdata[position].getName());
        holder.textView1.setText(listdata[position].getId());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Name : " + myListData.getName() +"\nId : " + myListData.getId(),Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView,textView1;
        public LinearLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.hname);
            this.textView1 = (TextView) itemView.findViewById(R.id.hid);
            this.relativeLayout = (LinearLayout) itemView.findViewById(R.id.li);
        }
    }
}