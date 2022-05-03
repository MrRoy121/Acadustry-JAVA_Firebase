package com.example.projectsubmitter.submission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.projectsubmitter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class indust_submission extends AppCompatActivity {

    FirebaseFirestore fs;
    String title, id, code;
    private RecyclerView courseRV;
    private ArrayList<my_submission_list> coursesArrayList;
    private indust_submission_adapter dapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indust_submission);

        TextView nsa  = findViewById(R.id.ns);
        fs = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("title");
            Log.e(title,"task.getResult().getId()");
        }
/*

        fs.collection("Submission").document().collection("Problems").document(title).collection("Student Id").document().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.e(document.getId(), String.valueOf(document));

                }
            }
        });

 */



        courseRV = findViewById(R.id.suview);
        coursesArrayList = new ArrayList<>();
        courseRV.setHasFixedSize(true);
        courseRV.setLayoutManager(new LinearLayoutManager(this));
        dapter = new indust_submission_adapter(coursesArrayList, this);
        courseRV.setAdapter(dapter);
        ArrayList<my_submission_list> listv = new ArrayList<>();
        fs.collection("Submission").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        fs.collection("Submission").document(document.getId()).collection("Problems").document(title).collection("Student Id").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot documentw : task.getResult()) {
                                        nsa.setVisibility(View.GONE);
                                        listv.add(new my_submission_list(document.getId(), title, documentw.getId(), "null", "null"));
                                        dapter= new indust_submission_adapter(listv, indust_submission.this);
                                        courseRV.setAdapter(dapter);
                                    }
                                    dapter.notifyDataSetChanged();
                                }
                            }
                        });


                    }
                }
            }
        });


    }

}