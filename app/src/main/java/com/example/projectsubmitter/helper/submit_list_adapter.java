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
import com.example.projectsubmitter.submission.submit2;

import java.util.ArrayList;

public class submit_list_adapter extends RecyclerView.Adapter<submit_list_adapter.ViewHolder> {
    // creating variables for our ArrayList and context
    private ArrayList<submit_list> coursesArrayList;
    private Context context;

    // creating constructor for our adapter class
    public submit_list_adapter(ArrayList<submit_list> coursesArrayList, Context context) {
        this.coursesArrayList = coursesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public submit_list_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new submit_list_adapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.submit_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        submit_list courses = coursesArrayList.get(position);
        holder.title.setText(courses.getTitle());
        holder.details.setText(courses.getDetails());
        holder.date.setText(courses.getDate());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, submit2.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", courses.getTitle());
                bundle.putString("roomcode", courses.getRan());
                bundle.putString("date", courses.getDate());
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

        private final TextView title;
        private final TextView details;
        private final TextView date;
        private final CardView relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            title = itemView.findViewById(R.id.ptitle);
            details = itemView.findViewById(R.id.pdetails);
            date = itemView.findViewById(R.id.pdate);
            relativeLayout = itemView.findViewById(R.id.linelayut);
        }
    }
}