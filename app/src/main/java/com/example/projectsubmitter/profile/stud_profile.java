package com.example.projectsubmitter.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectsubmitter.Login;
import com.example.projectsubmitter.R;
import com.example.projectsubmitter.classroom.cls_room;
import com.example.projectsubmitter.helper.stud_job_list;
import com.example.projectsubmitter.helper.stud_job_list_adapter;
import com.example.projectsubmitter.submission.stud_submissions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class stud_profile extends AppCompatActivity {

    FirebaseAuth fauth;
    FirebaseFirestore fs;
    CardView jroom;
    TextView sname, sfield;
    String useruid, name, email, xid, batch, roomcode;
    private stud_job_list_adapter adapter;
    LinearLayout subs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_profile);

        subs = findViewById(R.id.sub);
        jroom = findViewById(R.id.jroom);
        sname = findViewById(R.id.sname);
        sfield = findViewById(R.id.sfield);
        ImageButton logout = findViewById(R.id.logout);
        fauth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        if(fauth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), Login.class));
        }else{
            useruid = fauth.getCurrentUser().getUid();
        }

        subs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("id", xid);
                Intent intent = new Intent(stud_profile.this, stud_submissions.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Map<String, Object> docData = new HashMap<>();
        DocumentReference refer = fs.collection("Student").document(useruid);
        refer.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    name =  task.getResult().getString("Name");
                    xid =  task.getResult().getString("ID");
                    batch = task.getResult().getString("Batch");
                    email = task.getResult().getString("Email");
                    sname.setText(name);
                    sfield.setText(xid);
                    docData.put("StudentName", name);
                    docData.put("StudentID", xid);
                    docData.put("StudentEmail", email);
                    docData.put("Batch", batch);
                    joinlist(xid);
                    }
            }
        });

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refreshLayou);
        swipeRefreshLayout.setOnRefreshListener(() -> {
                    finish();
                    overridePendingTransition( 0, 0);
                    startActivity(getIntent());
                    overridePendingTransition( 0, 0);
                    swipeRefreshLayout.setRefreshing(false);
                }
        );

        jroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = new LinearLayout(stud_profile.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setGravity(Gravity.CENTER_HORIZONTAL);
                final EditText input = new EditText(stud_profile.this);
                input.setSingleLine(true);
                layout.setPadding(30, 0, 60, 0);
                input.setHint("Room Code");
                layout.addView(input);
                AlertDialog.Builder builder = new AlertDialog.Builder(stud_profile.this);
                builder.setView(layout);
                builder.setTitle("Join Inside The Room");
                builder.setCancelable(false).setPositiveButton("Join", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        roomcode = input.getText().toString();

                        Map<String, Object> city = new HashMap<>();
                        city.put("null", "null");

                        Query docIdRef = rootRef.collection("Rooms").whereEqualTo("random", roomcode);
                        docIdRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot document = task.getResult();
                                    if (!document.isEmpty()) {
                                        DocumentReference q = fs.collection("Room Members").document(roomcode);
                                        q.set(city);
                                        q.collection(roomcode).document(xid)
                                                        .set(docData)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                startActivity(getIntent());
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(getApplicationContext(),"Error writing document"+ e,Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(),"Room does not exist!",Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(),"Failed with: " + task.getException(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                builder.setCancelable(false).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fauth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }


    public void joinlist(String id){

        List<stud_job_list> lidst = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.studentView);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        fs.collection("Room Members").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }
                    for(int i = 0; i<list.size(); i++){
                        int finalI = i;
                        fs.collection("Room Members").document(list.get(i)).collection(list.get(i)).document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshots) {
                                if(documentSnapshots.exists()){
                                    lidst.add(new stud_job_list(list.get(finalI), name, xid));
                                    adapter= new stud_job_list_adapter(lidst,stud_profile.this);
                                }
                                recyclerView.setAdapter(adapter);
                            }
                        });
                    }
                }
            }
        });
    }

}