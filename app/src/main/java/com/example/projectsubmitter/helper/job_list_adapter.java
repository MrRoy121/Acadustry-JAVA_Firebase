package com.example.projectsubmitter.helper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;

import com.example.projectsubmitter.R;
import com.example.projectsubmitter.submission.indust_submission;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class job_list_adapter extends RecyclerView.Adapter<job_list_adapter.ViewHolder> {
    // creating variables for our ArrayList and context
    private ArrayList<job_list> coursesArrayList;
    private Context context;

    // creating constructor for our adapter class
    public job_list_adapter(ArrayList<job_list> coursesArrayList, Context context) {
        this.coursesArrayList = coursesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public job_list_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new job_list_adapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.post_job_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        job_list courses = coursesArrayList.get(position);
        holder.title.setText(courses.getTitle());
        holder.details.setText(courses.getDetails());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bc = new Bundle();
                bc.putString("title", courses.getTitle());
                Intent i = new Intent(context, indust_submission.class);
                i.putExtras(bc);
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

        private final TextView title;
        private final TextView details;
        private final CardView layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            title = itemView.findViewById(R.id.ptitle);
            details = itemView.findViewById(R.id.pdetails);
            layout = itemView.findViewById(R.id.linelayut);
        }
    }
}