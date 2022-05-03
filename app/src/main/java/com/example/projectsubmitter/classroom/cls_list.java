package com.example.projectsubmitter.classroom;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectsubmitter.R;
import com.example.projectsubmitter.helper.room_stud_list;
import com.example.projectsubmitter.helper.room_student_list_adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class cls_list extends AppCompatActivity {

    Boolean teacher;
    TextView cname, ctitle, cdetail, rand, tname, dlt, left;
    String name, title, detail, ran, tna;
    CardView copy, cv;
    FirebaseFirestore fs;
    private RecyclerView courseRV;
    private ArrayList<room_stud_list> coursesArrayList;
    private room_student_list_adapter dapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cls_list);

        fs = FirebaseFirestore.getInstance();
        cname = findViewById(R.id.lsec);
        ctitle = findViewById(R.id.ltil);
        cdetail = findViewById(R.id.lsub);
        rand = findViewById(R.id.lran);
        copy = findViewById(R.id.lcopy);
        tname = findViewById(R.id.tean);
        cv = findViewById(R.id.cv);
        dlt = findViewById(R.id.dlt);
        left = findViewById(R.id.left);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            teacher = bundle.getBoolean("Teacher");
            name = bundle.getString("sec");
            title = bundle.getString("title");
            detail = bundle.getString("sub");
            ran = bundle.getString("ran");
            tna = bundle.getString("tname");
            rand.setText(ran);
            cname.setText(name);
            ctitle.setText(title);
            tname.setText(tna);
            cdetail.setText(detail);
        }

        if(teacher){
            dlt.setVisibility(View.VISIBLE);
            left.setVisibility(View.INVISIBLE);
        }

        courseRV = findViewById(R.id.stdView);
        coursesArrayList = new ArrayList<>();
        courseRV.setHasFixedSize(true);
        courseRV.setLayoutManager(new LinearLayoutManager(this));
        dapter = new room_student_list_adapter(coursesArrayList, this);
        courseRV.setAdapter(dapter);
        fs.collection("Room Members").document(ran).collection(ran).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<room_stud_list> listv = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    listv.add(new room_stud_list(document.getString("StudentName"),document.getId()));
                    dapter= new room_student_list_adapter(listv, cls_list.this);
                    courseRV.setAdapter(dapter);
                }
                dapter.notifyDataSetChanged();
            }
        });

    }
}