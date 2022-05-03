package com.example.projectsubmitter.submission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.projectsubmitter.R;
import com.example.projectsubmitter.helper.submit_list;
import com.example.projectsubmitter.helper.submit_list_adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class submit extends AppCompatActivity{

    private ArrayList<submit_list> coursesrrayList;
    private submit_list_adapter courseRVdaptr;
    FirebaseFirestore fs;
    String ran;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        fs = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ran = extras.getString("key");
            listy(ran);
        }
    }

    public void listy(String ran){
        RecyclerView courseVR = findViewById(R.id.list);
        coursesrrayList = new ArrayList<>();
        courseVR.setHasFixedSize(true);
        courseVR.setLayoutManager(new LinearLayoutManager(this));
        courseRVdaptr = new submit_list_adapter(coursesrrayList, this);
        courseVR.setAdapter(courseRVdaptr);
        fs.collection("Room jobs").document(ran).collection(ran).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<submit_list> listv = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    listv.add(new submit_list(document.getString("JobTitle"),document.getString("CompanyName"),ran, document.getString("Date")));
                    courseRVdaptr= new submit_list_adapter(listv, submit.this);
                    courseVR.setAdapter(courseRVdaptr);
                }
                courseRVdaptr.notifyDataSetChanged();
            }
        });
    }
}
