package com.example.projectsubmitter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectsubmitter.classroom.cls_room;
import com.example.projectsubmitter.helper.room_helper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;


public class teach_room_create extends AppCompatActivity {

    FirebaseFirestore fstore;
    TextView pn, pe;
    EditText tle, sec, sub, rom;
    Button room;
    String ran;
    Editable roomw,dept,section,title;
    String tname;
    String temail;
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";
    FirebaseFirestore fs;
    FirebaseAuth fa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach_room_create);


        fstore = FirebaseFirestore.getInstance();
        tle = findViewById(R.id.tle);
        sec = findViewById(R.id.section);
        sub = findViewById(R.id.dept);
        rom = findViewById(R.id.roomno);
        pn = findViewById(R.id.pn);
        pe = findViewById(R.id.pe);

        roomw = rom.getText();
        dept = sub.getText();
        section = sec.getText();
        title = tle.getText();

        fa = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        String userid = fa.getCurrentUser().getUid();
        DocumentReference refer = fstore.collection("Teacher").document(userid);
        refer.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    tname =  task.getResult().getString("Name");
                    temail =  task.getResult().getString("Email");
                    pe.setText(temail);
                    pn.setText(tname);
                }
            }
        });

        room = findViewById(R.id.room);
        room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                            new AlertDialog.Builder(teach_room_create.this)
                                    .setTitle("Create New Room")
                                    .setMessage("Are you sure you want to create new room?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            ran = getRandomString(8);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("ran", ran);
                                            addDataToFirestore(ran, String.valueOf(title), String.valueOf(roomw), String.valueOf(dept), String.valueOf(section), tname, temail);

                                            Intent i = new Intent(teach_room_create.this, cls_room.class);
                                            bundle.putBoolean("Teacher", true);
                                            bundle.putString("Name", tname);
                                            i.putExtras(bundle);
                                            startActivity(i);
                                            finish();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(R.drawable.new_fold)
                                    .show();
                        }
        });
    }

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }


    private void addDataToFirestore(String rand, String title, String room, String dept, String section, String tname, String temail) {
        CollectionReference dbCourses = fstore.collection("Rooms");
        room_helper helper = new room_helper(rand, title, room, dept, section, tname, temail);


        dbCourses.add(helper).addOnSuccessListener(documentReference -> {
            Toast.makeText(teach_room_create.this, "Your Room has been added to database", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(teach_room_create.this, "Fail to add Room \n" + e, Toast.LENGTH_SHORT).show();
        });
    }
}