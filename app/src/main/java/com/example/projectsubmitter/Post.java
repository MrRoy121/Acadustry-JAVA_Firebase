package com.example.projectsubmitter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectsubmitter.helper.post_helper;
import com.example.projectsubmitter.profile.indust_profile;
import com.example.projectsubmitter.profile.teach_profile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Post extends AppCompatActivity {

    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String name;
    String email;

    List<String> names, where;
    ArrayAdapter<String> adapter;
    ListView listView;
    EditText tagg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        tagg = findViewById(R.id.tags);
        tagg.setEnabled(false);
        TextView pname = findViewById(R.id.pname);
        TextView pemail = findViewById(R.id.pemail);
        TextView tag = findViewById(R.id.tag);

        fstore = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();

        String userid = Objects.requireNonNull(fauth.getCurrentUser()).getUid();
        DocumentReference refer = fstore.collection("Industry").document(userid);

        refer.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                name =  task.getResult().getString("Name");
                email =  task.getResult().getString("Email");
                pname.setText(name);
                pemail.setText(email);
            }
        });

        names = new ArrayList<String>();
        fstore.collection("Tag").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshot) {
                if(!documentSnapshot.isEmpty()){
                    for(DocumentSnapshot d : documentSnapshot){
                        names.add(d.getId());
                    }
                }
            }
        });
        where = new ArrayList<String>();
        tag.setOnClickListener(v -> {
            taggg();
        });

        EditText wdetails = findViewById(R.id.detils);
        EditText txttit = findViewById(R.id.tile);
        Editable qtitle = txttit.getText();
        Editable qdetails = wdetails.getText();

        Button post = findViewById(R.id.post);
        post.setOnClickListener(v -> {
            if(qtitle.length() == 0 && qdetails.length()==0 && where.size()==0){
                Toast.makeText(getApplicationContext(),"All Fields are Required" ,Toast.LENGTH_SHORT).show();
            }else{
                String title = qtitle.toString();
                String details = qdetails.toString();

                CollectionReference dbCourses = fstore.collection("All Jobs");
                Map<String, Object> sss = new HashMap<>();
                sss.put("details", details);
                sss.put("userID", userid);
                sss.put("userName", name);
                sss.put("tags", where);
                sss.put("title", title);

                dbCourses.add(sss).addOnSuccessListener(documentReference -> {
                    Toast.makeText(Post.this, "Your Job has been added to database", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), indust_profile.class));
                    finish();
                }).addOnFailureListener(e -> {
                    Toast.makeText(Post.this, "Fail to add job \n" + e, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void taggg(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Post.this);
        View rowList = getLayoutInflater().inflate(R.layout.row, null);
        listView = rowList.findViewById(R.id.listView);
        Button btn = rowList.findViewById(R.id.btn);
        TextView tv = rowList.findViewById(R.id.tv);
        adapter =  new ArrayAdapter(Post.this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, names);
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
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!where.isEmpty()){
                    tagg.setText(String.valueOf(where));
                    dialog.dismiss();
                }else
                    Toast.makeText(Post.this, "Select At least One Tag", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

}