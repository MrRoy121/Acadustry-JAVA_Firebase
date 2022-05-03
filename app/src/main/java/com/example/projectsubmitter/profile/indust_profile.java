package com.example.projectsubmitter.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectsubmitter.Login;
import com.example.projectsubmitter.Post;
import com.example.projectsubmitter.R;
import com.example.projectsubmitter.helper.job_list;
import com.example.projectsubmitter.helper.job_list_adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
public class indust_profile extends AppCompatActivity {

    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String name, field;
    private RecyclerView courseRV;
    private ArrayList<job_list> coursesArrayList;
    private job_list_adapter ad;

    int val = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indust_profile);

        TextView valo = findViewById(R.id.vali);
        TextView rname = findViewById(R.id.iname);
        TextView rfield = findViewById(R.id.ifield);
        fstore = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();
        String userid = fauth.getCurrentUser().getUid();

        fauth = FirebaseAuth.getInstance();
        if(fauth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
        courseRV = findViewById(R.id.recyclerView);
        coursesArrayList = new ArrayList<>();
        courseRV.setHasFixedSize(true);
        courseRV.setLayoutManager(new LinearLayoutManager(this));
        ad = new job_list_adapter(coursesArrayList, this);
        fstore.collection("All Jobs").whereEqualTo("userID", userid).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                val++;
                                job_list c = d.toObject(job_list.class);
                                coursesArrayList.add(c);
                            }
                            valo.setText(String.valueOf(val));
                            ad.notifyDataSetChanged();
                        } else {
                            Toast.makeText(indust_profile.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(indust_profile.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refreshLayou);
        swipeRefreshLayout.setOnRefreshListener(() -> {
                    finish();
                    overridePendingTransition( 0, 0);
                    startActivity(getIntent());
                    overridePendingTransition( 0, 0);
                    swipeRefreshLayout.setRefreshing(false);
                }
        );
        courseRV.setAdapter(ad);
        DocumentReference refer = fstore.collection("Industry").document(userid);

        refer.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    name =  task.getResult().getString("Name");
                    field =  task.getResult().getString("Field");
                    rname.setText(name);
                    rfield.setText(field);
                }
            }
        });

        CardView uppost = findViewById(R.id.uppost);
        uppost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Post.class));
                finish();
            }
        });


        ImageButton logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fauth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }
}