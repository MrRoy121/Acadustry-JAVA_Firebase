package com.example.projectsubmitter.helper;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectsubmitter.R;
import com.example.projectsubmitter.classroom.cls_job_list;
import com.example.projectsubmitter.classroom.cls_room;
import com.example.projectsubmitter.profile.stud_profile;
import com.example.projectsubmitter.submission.submission2;
import com.example.projectsubmitter.submission.submit2;
import com.example.projectsubmitter.teach_room_create;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class teach_job_list_adapter extends RecyclerView.Adapter<teach_job_list_adapter.ViewHolder> {
    // creating variables for our ArrayList and context
    private ArrayList<teach_job_list> coursesArrayList;
    private Context context;

    // creating constructor for our adapter class
    public teach_job_list_adapter(ArrayList<teach_job_list> coursesArrayList, Context context) {
        this.coursesArrayList = coursesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public teach_job_list_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.teach_job_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull teach_job_list_adapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        teach_job_list courses = coursesArrayList.get(position);
        holder.courseName.setText(courses.getuserName());
        holder.coursetitle.setText(courses.getTitle());
        String n = courses.getuserName();
        String a = courses.getTitle();


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.job_details2);
                TextView t = dialog.findViewById(R.id.title);
                TextView dd = dialog.findViewById(R.id.details);
                t.setText(a);
                FirebaseFirestore.getInstance().collection("All Jobs").whereEqualTo("title",a).whereEqualTo("userName",n).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot q) {
                        for (DocumentSnapshot d : q){
                            dd.setText(d.getString("details"));
                        }
                        dialog.show();
                    }
                });
                Button dialogButton = dialog.findViewById(R.id.add);
                dialogButton.setOnClickListener(v -> {
                    dialog.dismiss();
                    LinearLayout layout = new LinearLayout(context);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setGravity(Gravity.CENTER_HORIZONTAL);
                    final DatePicker simpleDatePicker = new DatePicker(context);

                    layout.setPadding(30, 0, 60, 0);
                    layout.addView(simpleDatePicker);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Add Job To Classroom").setMessage("Last Submission Date");
                    builder.setView(layout)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog1, int which) {
                                    SharedPreferences preferences = context.getSharedPreferences("MySharedPref1", Context.MODE_PRIVATE);
                                    String random = preferences.getString("ran", "");

                                    String day = "" + simpleDatePicker.getDayOfMonth();
                                    String month = "" + (simpleDatePicker.getMonth() + 1);
                                    String year = "" + simpleDatePicker.getYear();

                                    Map<String, Object> city = new HashMap<>();
                                    city.put("null", "null");
                                    Log.e("TAG", day + "-" + month + "-" + year);

                                    Map<String, Object> docData = new HashMap<>();
                                    docData.put("CompanyName", n);
                                    docData.put("JobTitle", a);
                                    docData.put("Date",  day + "-" + month + "-" + year);

                                    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

                                    rootRef.collection("All Jobs").whereEqualTo("userName",n).whereEqualTo("title",a).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                            for (DocumentSnapshot d : list) {
                                                String xid =  d.getId();
                                                Log.e("Tag2", random);
                                                Query docIdRef = rootRef.collection("Rooms").whereEqualTo("random", random);
                                                docIdRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.e("Tag2", random);
                                                            QuerySnapshot document = task.getResult();
                                                            if (!document.isEmpty()) {
                                                                DocumentReference q = rootRef.collection("Room jobs").document(random);
                                                                q.set(city);
                                                                q.collection(random).document(xid)
                                                                        .set(docData)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Toast.makeText(context,"Added",Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(context,"Error writing document"+ e,Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            } else {
                                                                Toast.makeText(context,"Room does not exist!",Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(context,"Failed with: " + task.getException(),Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                            }
                                        }
                                    });
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.new_fold)
                            .show();
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return coursesArrayList.size();
    }

    public void clear() {
        int size = coursesArrayList.size();
        coursesArrayList.clear();
        notifyItemRangeRemoved(0, size);
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView courseName;
        private final TextView coursetitle;
        private final CardView relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            courseName = itemView.findViewById(R.id.pcname);
            coursetitle = itemView.findViewById(R.id.ptitle);
            relativeLayout = itemView.findViewById(R.id.linelayout);
        }
    }
}