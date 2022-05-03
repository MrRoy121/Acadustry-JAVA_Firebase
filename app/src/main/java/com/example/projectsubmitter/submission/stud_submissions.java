package com.example.projectsubmitter.submission;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectsubmitter.R;
import com.example.projectsubmitter.classroom.cls_list;
import com.example.projectsubmitter.helper.room_stud_list;
import com.example.projectsubmitter.helper.room_student_list_adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class stud_submissions extends AppCompatActivity {

    String id;

    private RecyclerView courseRV;
    private ArrayList<my_submission_list> coursesArrayList;
    private my_submission_list_adapter dapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_submissions);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getString("id")==null){

            }else
                id = extras.getString("id");
        }

        courseRV = findViewById(R.id.slist);
        coursesArrayList = new ArrayList<>();
        courseRV.setHasFixedSize(true);
        courseRV.setLayoutManager(new LinearLayoutManager(this));
        dapter = new my_submission_list_adapter(coursesArrayList, this);
        courseRV.setAdapter(dapter);

        ArrayList<my_submission_list> listv = new ArrayList<>();
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        fs.collection("Submission").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    fs.collection("Submission").document(document.getId()).collection("Problems").get().addOnCompleteListener(taskc -> {
                        if (taskc.isSuccessful()) {
                            for (QueryDocumentSnapshot documentc : taskc.getResult()) {
                                fs.collection("Submission").document(document.getId()).collection("Problems").document(documentc.getId()).collection("Student Id").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> taskcx) {
                                        if (taskcx.isSuccessful()) {
                                            listv.add(new my_submission_list(document.getId(), documentc.getId(), taskcx.getResult().getString("Submission Date"), taskcx.getResult().getString("Mode"),id));
                                            dapter= new my_submission_list_adapter(listv, stud_submissions.this);
                                            courseRV.setAdapter(dapter);
                                        }
                                        dapter.notifyDataSetChanged();
                                    }

                                });
                            }
                        }
                    });
                }
            }
    });
    }
}