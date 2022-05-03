package com.example.projectsubmitter.helper;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectsubmitter.R;
import com.example.projectsubmitter.classroom.cls_job_list;
import com.example.projectsubmitter.classroom.cls_room;
import com.example.projectsubmitter.submission.indust_submission;
import com.example.projectsubmitter.submission.submission2;
import com.example.projectsubmitter.submission.submissions;
import com.example.projectsubmitter.submission.submit;
import com.example.projectsubmitter.submission.submit2;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class job_list_adapter2 extends RecyclerView.Adapter<job_list_adapter2.ViewHolder> {
    private ArrayList<job_list> coursesArrayList;
    private Context context;

    public job_list_adapter2(ArrayList<job_list> coursesArrayList, Context context) {
        this.coursesArrayList = coursesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public job_list_adapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new job_list_adapter2.ViewHolder(LayoutInflater.from(context).inflate(R.layout.post_job_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        job_list courses = coursesArrayList.get(position);
        holder.title.setText(courses.getDetails());
        holder.details.setText(courses.getTitle());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.job_details);
                TextView t = dialog.findViewById(R.id.title);
                TextView ld = dialog.findViewById(R.id.ld);
                TextView dd = dialog.findViewById(R.id.details);
                t.setText(courses.getDetails());
                FirebaseFirestore.getInstance().collection("All Jobs").whereEqualTo("title",courses.getDetails()).whereEqualTo("userName",courses.getTitle()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot q) {
                        for (DocumentSnapshot d : q){
                            dd.setText(d.getString("details"));
                        }
                        FirebaseFirestore.getInstance().collection("Room jobs").document(courses.getRan()).collection(courses.getRan()).whereEqualTo("JobTitle",courses.getDetails()).whereEqualTo("CompanyName",courses.getTitle()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot q) {
                                for (DocumentSnapshot d : q){
                                    ld.setText(d.getString("Date"));
                                }
                                dialog.show();
                            }
                        });
                    }
                });
                Button dialogButton2 = (Button) dialog.findViewById(R.id.s2);
                Button dialogButton = (Button) dialog.findViewById(R.id.s1);
                if(courses.isIs()){
                    dialogButton2.setVisibility(View.GONE);
                }else {
                    dialogButton.setVisibility(View.GONE);
                }
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent i = new Intent(context, submit2.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("title", courses.getDetails());
                        bundle.putString("roomcode", courses.getRan());
                        bundle.putString("date", ld.getText().toString());
                        i.putExtras(bundle);
                        context.startActivity(i);
                    }
                });
                dialogButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent i = new Intent(context, submission2.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("ran", courses.getRan());
                        bundle.putString("title", courses.getDetails());
                        i.putExtras(bundle);
                        context.startActivity(i);
                    }
                });
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