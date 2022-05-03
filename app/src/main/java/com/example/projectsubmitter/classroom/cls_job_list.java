package com.example.projectsubmitter.classroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectsubmitter.Post;
import com.example.projectsubmitter.R;
import com.example.projectsubmitter.helper.job_list;
import com.example.projectsubmitter.helper.job_list_adapter;
import com.example.projectsubmitter.helper.job_list_adapter2;
import com.example.projectsubmitter.helper.room_stud_list;
import com.example.projectsubmitter.helper.room_student_list_adapter;
import com.example.projectsubmitter.helper.teach_job_list;
import com.example.projectsubmitter.helper.teach_job_list_adapter;
import com.example.projectsubmitter.helper.techer_room_adapter;
import com.example.projectsubmitter.helper.techer_room_list;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class cls_job_list extends AppCompatActivity {

    TextView cname, ctitle, cdetail, rand, tname;
    String name, title, detail, ran, tna;
    CardView copy;
    FirebaseFirestore fs;
    FrameLayout fm;
    TextView tags;
    private RecyclerView allrv, courseVR;
    private ArrayList<teach_job_list> all;
    private teach_job_list_adapter alla;

    List<String> names, where;
    ArrayAdapter<String> adapter;
    ListView listView;
    Boolean is = false;

    private job_list_adapter2 courseRVdaptr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cls_job_list);



        Button cjobs = findViewById(R.id.ccjobs);
        Button ajobs = findViewById(R.id.aajobs);
        fs = FirebaseFirestore.getInstance();
        cname = findViewById(R.id.lsec);
        ctitle = findViewById(R.id.ltil);
        cdetail = findViewById(R.id.lsub);
        rand = findViewById(R.id.lran);
        copy = findViewById(R.id.lcopy);
        tname = findViewById(R.id.tean);
        allrv = findViewById(R.id.ajobs);
        courseVR = findViewById(R.id.cjobsc);
        fm = findViewById(R.id.fm);
        tags = findViewById(R.id.tags);

        names = new ArrayList<>();
        where = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref1",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if(bundle.getBoolean("ad")){
                ran = bundle.getString("ran");
                rand.setText(ran);

            }else{
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
                myEdit.putString("ran", ran);
                myEdit.apply();
            }
            if (!bundle.getBoolean("Teacher")) {
                Log.e(String.valueOf(bundle.getBoolean("Teacher")),"ssas");
                ajobs.setVisibility(View.INVISIBLE);
                is=true;
            }
        }

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refreshLayou);
        swipeRefreshLayout.setOnRefreshListener(() -> {
                    finish();
                    overridePendingTransition( 0, 0);
                    startActivity(getIntent());
                    overridePendingTransition( 0, 0);
                    swipeRefreshLayout.setRefreshing(false);
                }
        );


        ajobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseVR.setVisibility(View.INVISIBLE);
                allrv.setVisibility(View.VISIBLE);
                fm.setVisibility(View.VISIBLE);
            }
        });

        cjobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseVR.setVisibility(View.VISIBLE);
                allrv.setVisibility(View.INVISIBLE);
                fm.setVisibility(View.INVISIBLE);
            }
        });

        fs.collection("Tag").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshot) {
                if(!documentSnapshot.isEmpty()){
                    for(DocumentSnapshot d : documentSnapshot){
                        names.add(d.getId());
                    }
                }
            }
        });
        tags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(cls_job_list.this);
                View rowList = getLayoutInflater().inflate(R.layout.row, null);
                listView = rowList.findViewById(R.id.listView);
                Button btn = rowList.findViewById(R.id.btn);
                TextView tv = rowList.findViewById(R.id.tv);
                adapter =  new ArrayAdapter(cls_job_list.this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, names);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CheckedTextView v = (CheckedTextView) view;
                        if(!v.isChecked()){
                            String selectedItem = (String) listView.getItemAtPosition(position);
                            where.add(selectedItem);
                            v.setChecked(true);
                            tv.setText(String.valueOf(where));

                        }else{
                            String selectedItem = (String) listView.getItemAtPosition(position);
                            where.remove(selectedItem);
                            v.setChecked(false);
                            tv.setText(String.valueOf(where));
                        }

                    }
                });
                adapter.notifyDataSetChanged();
                alertDialog.setView(rowList);
                AlertDialog dialog = alertDialog.create();
                btn.setOnClickListener(v1 -> {
                    if(!where.isEmpty()){
                        tags.setText(String.valueOf(where));
                        dialog.dismiss();
                        rv();
                    }else
                        Toast.makeText(cls_job_list.this, "Select At least One Tag", Toast.LENGTH_SHORT).show();
                });
                dialog.show();
            }
        });


        ArrayList<job_list> coursesrrayList = new ArrayList<>();
        courseVR.setHasFixedSize(true);
        courseVR.setLayoutManager(new LinearLayoutManager(this));
        courseRVdaptr = new job_list_adapter2(coursesrrayList, this);
        courseVR.setAdapter(courseRVdaptr);

        all = new ArrayList<>();
        allrv.setHasFixedSize(true);
        allrv.setLayoutManager(new LinearLayoutManager(this));

        alla = new teach_job_list_adapter(all, this);

        allrv.setAdapter(alla);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("All Jobs").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                teach_job_list c = d.toObject(teach_job_list.class);
                                all.add(c);
                            }
                            alla.notifyDataSetChanged();
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText(cls_job_list.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(cls_job_list.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });

        fs.collection("Room jobs").document(ran).collection(ran).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<job_list> listv = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    listv.add(new job_list(document.getString("CompanyName"),document.getString("JobTitle"),ran,is));
                    courseRVdaptr= new job_list_adapter2(listv, cls_job_list.this);
                    courseVR.setAdapter(courseRVdaptr);
                }
                courseRVdaptr.notifyDataSetChanged();
            }
        });

    }

    private void rv(){
        alla.clear();
            fs.collection("All Jobs").whereArrayContainsAny("tags", where).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            teach_job_list c = d.toObject(teach_job_list.class);
                            all.add(c);
                        }
                        alla.notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(cls_job_list.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }
}