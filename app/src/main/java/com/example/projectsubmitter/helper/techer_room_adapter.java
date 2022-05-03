package com.example.projectsubmitter.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectsubmitter.R;
import com.example.projectsubmitter.classroom.cls_room;

import java.util.ArrayList;

public class techer_room_adapter extends RecyclerView.Adapter<techer_room_adapter.ViewHolder> {
    // creating variables for our ArrayList and context
    private ArrayList<techer_room_list> coursesArrayList;
    private Context context;

    // creating constructor for our adapter class
    public techer_room_adapter(ArrayList<techer_room_list> coursesArrayList, Context context) {
        this.coursesArrayList = coursesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public techer_room_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.techer_room_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull techer_room_adapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        techer_room_list courses = coursesArrayList.get(position);
        holder.title.setText(courses.getTitle());
        holder.tname.setText(courses.getteacherName());
        holder.subject.setText(courses.getSubject());;
        holder.section.setText(courses.getSection());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, cls_room.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("Teacher", true);
                bundle.putString("Name", courses.getteacherName());
                bundle.putString("ran", courses.getRandom());
                i.putExtras(bundle);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return coursesArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView title;
        private final TextView tname;
        private final TextView section;
        private final TextView subject;
        private final CardView relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            title = itemView.findViewById(R.id.pitle);
            tname = itemView.findViewById(R.id.pcname);
            section = itemView.findViewById(R.id.sec);
            subject = itemView.findViewById(R.id.subject);
            relativeLayout = itemView.findViewById(R.id.layout);
        }
    }
}
