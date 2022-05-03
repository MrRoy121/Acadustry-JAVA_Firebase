package com.example.projectsubmitter.submission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.projectsubmitter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class submissions extends AppCompatActivity {

    private RecyclerView courseRV;
    private ArrayList<my_submission_list> coursesArrayList;
    private prob_submission_adapter dapter;
    String roomcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submissions);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            roomcode = extras.getString("key");
        }
        courseRV = findViewById(R.id.subview);
        coursesArrayList = new ArrayList<>();
        courseRV.setHasFixedSize(true);
        courseRV.setLayoutManager(new LinearLayoutManager(this));
        dapter = new prob_submission_adapter(coursesArrayList, this);
        courseRV.setAdapter(dapter);

        ArrayList<my_submission_list> listv = new ArrayList<>();
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        fs.collection("Submission").document(roomcode).collection("Problems").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                                listv.add(new my_submission_list(roomcode, document.getId(), "null", "null", "null"));
                                dapter= new prob_submission_adapter(listv, submissions.this);
                                courseRV.setAdapter(dapter);
                            }
                            dapter.notifyDataSetChanged();
                }
        });

    }



}