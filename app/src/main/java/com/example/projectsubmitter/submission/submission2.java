package com.example.projectsubmitter.submission;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.projectsubmitter.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class submission2 extends AppCompatActivity {

    String roomcode,title;
    TextView jt, ts, sm, ls;
    private RecyclerView courseRV, courseRV1,courseRV2;
    private ArrayList<my_submission_list> coursesArrayList, coursesArrayList1, coursesArrayList2;
    private stud_list_submission_adapter dapter, dapter1, dapter2;
    FirebaseFirestore fs;
    int sl = 0, ssl = 0, lsl = 0;
    Button all, on, late;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission2);

        all = findViewById(R.id.all);
        on = findViewById(R.id.on);
        late = findViewById(R.id.late);
        fs = FirebaseFirestore.getInstance();
        jt = findViewById(R.id.jt);
        ts = findViewById(R.id.ts);
        sm = findViewById(R.id.sm);
        ls = findViewById(R.id.ls);


        courseRV1 = findViewById(R.id.submview1);
        courseRV2 = findViewById(R.id.submview2);
        courseRV = findViewById(R.id.submview);
        coursesArrayList = new ArrayList<>();
        coursesArrayList1 = new ArrayList<>();
        coursesArrayList2 = new ArrayList<>();
        courseRV.setHasFixedSize(true);
        courseRV.setLayoutManager(new LinearLayoutManager(this));
        courseRV1.setHasFixedSize(true);
        courseRV1.setLayoutManager(new LinearLayoutManager(this));
        courseRV2.setHasFixedSize(true);
        courseRV2.setLayoutManager(new LinearLayoutManager(this));
        dapter = new stud_list_submission_adapter(coursesArrayList, this);
        dapter1 = new stud_list_submission_adapter(coursesArrayList1, this);
        dapter2 = new stud_list_submission_adapter(coursesArrayList2, this);
        ArrayList<my_submission_list> listv = new ArrayList<>();
        ArrayList<my_submission_list> listv1 = new ArrayList<>();
        ArrayList<my_submission_list> listv2 = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            roomcode = bundle.getString("ran");
            title = bundle.getString("title");
            jt.setText(title);
            fs.collection("Submission").document(roomcode).collection("Problems").document(title).collection("Student Id").whereEqualTo("Mode","Late Submission").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot qs) {
                    if(!qs.isEmpty()){
                        dapter2.clear();
                        for(DocumentSnapshot document: qs){
                            listv2.add(new my_submission_list(roomcode, title, document.getId(), document.getString("Mode"),  document.getString("Submission Date")));
                            dapter2= new stud_list_submission_adapter(listv2, submission2.this);
                            courseRV2.setAdapter(dapter2);
                            lsl = lsl + 1;
                        }
                        ls.setText(String.valueOf(lsl));
                    }
                }
            });

            fs.collection("Submission").document(roomcode).collection("Problems").document(title).collection("Student Id").whereEqualTo("Mode","Submission On Time").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot qs) {
                    if(!qs.isEmpty()){
                        dapter1.clear();
                        for(DocumentSnapshot document: qs){
                            listv1.add(new my_submission_list(roomcode, title, document.getId(), document.getString("Mode"),  document.getString("Submission Date")));
                            Log.e(document.getString("Mode")+"2", document.getString("Submission Date"));
                            dapter1= new stud_list_submission_adapter(listv1, submission2.this);
                            courseRV1.setAdapter(dapter1);
                        }
                    }
                }
            });
            fs.collection("Room Members").document(roomcode).collection(roomcode).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {
                                    sl++;
                                }
                                ts.setText(String.valueOf(sl));
                            }
                        }
                    });
        }

        fs.collection("Submission").document(roomcode).collection("Problems").document(title).collection("Student Id").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                dapter.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    listv.add(new my_submission_list(roomcode, title, document.getId(), document.getString("Mode"),  document.getString("Submission Date")));
                    Log.e(document.getString("Mode")+"1", document.getString("Submission Date"));
                    dapter= new stud_list_submission_adapter(listv, submission2.this);
                    courseRV.setAdapter(dapter);
                    ssl = ssl +1;
                }
                sm.setText(String.valueOf(ssl));
                dapter.notifyDataSetChanged();
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseRV.setVisibility(View.VISIBLE);
                courseRV1.setVisibility(View.INVISIBLE);
                courseRV2.setVisibility(View.INVISIBLE);
            }
        });

        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseRV1.setVisibility(View.VISIBLE);
                courseRV.setVisibility(View.INVISIBLE);
                courseRV2.setVisibility(View.INVISIBLE);
            }
        });
        late.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseRV2.setVisibility(View.VISIBLE);
                courseRV1.setVisibility(View.INVISIBLE);
                courseRV.setVisibility(View.INVISIBLE);
            }
        });
    }
}