package com.example.projectsubmitter.profile;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.projectsubmitter.Login;
import com.example.projectsubmitter.R;
import com.example.projectsubmitter.classroom.cls_room;
import com.example.projectsubmitter.helper.teach_job_list;
import com.example.projectsubmitter.helper.teach_job_list_adapter;
import com.example.projectsubmitter.helper.techer_room_adapter;
import com.example.projectsubmitter.helper.techer_room_list;
import com.example.projectsubmitter.submission.submissions;
import com.example.projectsubmitter.teach_room_create;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class teach_profile extends AppCompatActivity {

    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String name, dept;
    CardView croom;
    LinearLayout tag;
    int cal = 0;

    private static final int STORAGE_PERMISSION_CODE = 101;

    private RecyclerView courseVR;

    private ArrayList<techer_room_list> coursesrrayList;
    private techer_room_adapter courseRVdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach_profile);


        fstore = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();
        String userid = fauth.getCurrentUser().getUid();
        if(fauth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
        tag = findViewById(R.id.tag);
        TextView rname = findViewById(R.id.tname);
        TextView rdept = findViewById(R.id.tdept);
        croom = findViewById(R.id.ccroom);
        courseVR = findViewById(R.id.aarooms);


        TextView jab = findViewById(R.id.tagy);
        fstore.collection("Tag").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                cal++;
                            }
                            jab.setText(String.valueOf(cal));
                        }
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
        croom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(teach_profile.this, teach_room_create.class);
                startActivity(i);

            }
        });

        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = new LinearLayout(teach_profile.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setGravity(Gravity.CENTER_HORIZONTAL);
                final EditText input = new EditText(teach_profile.this);
                input.setSingleLine(true);
                layout.setPadding(30, 0, 60, 0);
                input.setHint("Tag");
                layout.addView(input);
                AlertDialog.Builder builder = new AlertDialog.Builder(teach_profile.this);
                builder.setView(layout);
                builder.setTitle("Add New Tags");
                builder.setCancelable(false).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(!input.getText().toString().equals("")){
                            Map<String, Object> tg = new HashMap<>();
                            tg.put("null", "null");
                            fstore.collection("Tag").document(input.getText().toString()).set(tg).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(teach_profile.this, "Tag Added", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else
                            Toast.makeText(teach_profile.this, "Write A Tag!!", Toast.LENGTH_SHORT).show();
                      }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        DocumentReference refer = fstore.collection("Teacher").document(userid);
        refer.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    name =  task.getResult().getString("Name");
                    dept =  task.getResult().getString("Department");
                    rname.setText(name);
                    rdept.setText(dept);
                    fstoreupdate();
                }
            }
        });


        coursesrrayList = new ArrayList<>();
        courseVR.setHasFixedSize(true);
        courseVR.setLayoutManager(new LinearLayoutManager(this));
        courseRVdapter = new techer_room_adapter(coursesrrayList, this);
        courseVR.setAdapter(courseRVdapter);


        fauth = FirebaseAuth.getInstance();
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

    public void fstoreupdate(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Rooms").whereEqualTo("teacherName", name).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                techer_room_list c = d.toObject(techer_room_list.class);
                                coursesrrayList.add(c);
                            }
                            courseRVdapter.notifyDataSetChanged();
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText(teach_profile.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // if we do not get any data or any error we are displaying
                // a toast message that we do not get any data
                Toast.makeText(teach_profile.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });

    } private void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(teach_profile.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(teach_profile.this, new String[] { permission }, requestCode);
        }
    }
}

