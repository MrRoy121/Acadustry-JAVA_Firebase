package com.example.projectsubmitter.classroom;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectsubmitter.R;
import com.example.projectsubmitter.helper.job_list;
import com.example.projectsubmitter.helper.message_adapter;
import com.example.projectsubmitter.helper.post_helper;
import com.example.projectsubmitter.helper.techer_room_list;
import com.example.projectsubmitter.profile.indust_profile;
import com.example.projectsubmitter.submission.stud_list_submission_adapter;
import com.example.projectsubmitter.submission.submission2;
import com.example.projectsubmitter.submission.submissions;
import com.example.projectsubmitter.submission.submit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class cls_room extends AppCompatActivity {

    EditText messageArea;
    ImageButton sendText;
    String message, name, id;
    LinearLayout sident, joblist;
    TextView cname, ctitle, cdetail, rand;
    String sec, title, sub, roomcode, tname;
    CardView copy, ucpt, ucpto;
    FirebaseFirestore fs;
    boolean teacher = false;
    private RecyclerView courseVR;

    int val = 0, cal = 0;

    private ArrayList<post_helper> coursesrrayList;
    private message_adapter courseRVdapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cls_room);

        messageArea = findViewById(R.id.messageArea);
        sendText = findViewById(R.id.sendButton);
        cname = findViewById(R.id.sec);
        ctitle = findViewById(R.id.til);
        cdetail = findViewById(R.id.sub);
        rand = findViewById(R.id.rand);
        copy = findViewById(R.id.copy);
        sident = findViewById(R.id.sid);
        joblist = findViewById(R.id.joblist);
        fs = FirebaseFirestore.getInstance();
        ucpt = findViewById(R.id.upt);
        ucpto = findViewById(R.id.upto);

        Map<String, Object> mesData = new HashMap<>();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
                roomcode = bundle.getString("ran");
                teacher = bundle.getBoolean("Teacher");
                fs.collection("Rooms").whereEqualTo("random", roomcode).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot q = task.getResult();
                        for (DocumentSnapshot c : q) {
                            sec = c.get("section").toString();
                            title = c.get("title").toString();
                            tname = c.get("teacherName").toString();
                            sub = c.get("subject").toString();
                            rand.setText(roomcode);
                            cname.setText(sec);
                            ctitle.setText(title);
                            cdetail.setText(sub);
                        }
                    }
                });
            if(!bundle.getBoolean("stud")) {
                name = bundle.getString("Name");
                Log.e("Tag", name);
                ucpt.setVisibility(View.GONE);
                ucpto.setVisibility(View.VISIBLE);
            }else{

                name = bundle.getString("Name");
                id = bundle.getString("Id");
                Log.e("Tag", name);
                ucpt.setVisibility(View.VISIBLE);
                ucpto.setVisibility(View.GONE);
            }
        }

        TextView valo = findViewById(R.id.stud);
        fs.collection("Room Members").document(roomcode).collection(roomcode).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                val++;
                            }
                            valo.setText(String.valueOf(val));
                        }
                    }
                });


        TextView jab = findViewById(R.id.jabs);
        fs.collection("Room jobs").document(roomcode).collection(roomcode).get()
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


        courseVR = findViewById(R.id.meaView);
        coursesrrayList = new ArrayList<post_helper>();
        courseVR.setHasFixedSize(true);
        courseVR.setLayoutManager(new LinearLayoutManager(this));


        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        copy.setOnClickListener(v -> {
            ClipData clip = ClipData.newPlainText("label", roomcode);
            Toast.makeText(getApplicationContext(),"Text Copied",Toast.LENGTH_SHORT).show();
            clipboard.setPrimaryClip(clip);
        });

        sident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("Teacher", teacher);
                bundle.putString("sec", sec);
                bundle.putString("title", title);
                bundle.putString("sub", sub);
                bundle.putString("ran", roomcode);
                bundle.putString("tname", tname);
                Intent intent = new Intent(cls_room.this, cls_list.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        joblist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("Teacher", teacher);
                bundle.putString("sec", sec);
                bundle.putString("title", title);
                bundle.putString("sub", sub);
                bundle.putString("ran", roomcode);
                bundle.putString("tname", tname);
                Intent intent = new Intent(cls_room.this, cls_job_list.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        ucpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(cls_room.this, submit.class);
                i.putExtra("key",roomcode);
                startActivity(i);
            }
        });
        ucpto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(cls_room.this, submissions.class);
                i.putExtra("key",roomcode);
                startActivity(i);
            }
        });

        sendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = String.valueOf(messageArea.getText());
                if(!message.equals("")){
                    Map<String, Object> noll = new HashMap<>();
                    noll.put("Null", null);
                    DateFormat df = new SimpleDateFormat("KK:mm dd/MM/yyyy", Locale.getDefault());
                    String c = df.format(new Date());
                    mesData.put("Name", name);
                    mesData.put("Message", message);
                    mesData.put("Time", c);

                    AlertDialog.Builder dialog = new AlertDialog.Builder(cls_room.this);
                    dialog.setTitle("Alert")
                            .setMessage("Send The Message")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                    dialoginterface.cancel();
                                }})
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                    DocumentReference s = fs.collection("Message").document(roomcode);
                                    s.set(noll);
                                    s.collection("List").document().set(mesData);
                                    startActivity(getIntent());
                                }
                            }).show();
                }
            }
        });



        fs.collection("Message").document(roomcode).collection("List").orderBy("Time", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshot) {
                if(!documentSnapshot.isEmpty()){
                    for (DocumentSnapshot d : documentSnapshot){
                            coursesrrayList.add(new post_helper(d.getString("Name"), d.getString("Message"), d.getString("Time"),"b",null));
                            courseRVdapter = new message_adapter(coursesrrayList, cls_room.this);
                            courseVR.setAdapter(courseRVdapter);
                    }
                        courseRVdapter.notifyDataSetChanged();
                }
            }
        });

    }
}