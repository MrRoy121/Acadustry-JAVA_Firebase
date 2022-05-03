package com.example.projectsubmitter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projectsubmitter.profile.indust_profile;
import com.example.projectsubmitter.profile.stud_profile;
import com.example.projectsubmitter.profile.teach_profile;
import com.example.projectsubmitter.register.indust_regi;
import com.example.projectsubmitter.register.stud_regi;
import com.example.projectsubmitter.register.teach_regi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Login extends AppCompatActivity {

    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    SharedPreferences sp;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String type = "type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fstore = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();
        EditText lemail = findViewById(R.id.email);
        EditText lpass = findViewById(R.id.pass);
        LinearLayout card = findViewById(R.id.card);
        TextView sign = findViewById(R.id.signup);
        LinearLayout layout = findViewById(R.id.layout);
        sp = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Bundle bundle = getIntent().getExtras();
        if(fauth.getCurrentUser() != null){
            if (bundle != null) {
                String value = bundle.getString("value");
                if (value.equals("Student")){
                    startActivity(new Intent(getApplicationContext(), stud_profile.class));
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(type, "Student");
                    editor.apply();
                }else if (value.equals("Teacher")){
                    startActivity(new Intent(getApplicationContext(), teach_profile.class));
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(type, "Teacher");
                    editor.apply();
                }else if (value.equals("Industry")){
                    startActivity(new Intent(getApplicationContext(), indust_profile.class));
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(type, "Industry");
                    editor.apply();
                }
            }
        }

        sign.setOnClickListener(v -> {
            layout.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = card.getLayoutParams();
            params.height = 900;
            card.setLayoutParams(params);
        });

        RelativeLayout blur = findViewById(R.id.blur);
        Button login = findViewById(R.id.login);
        Button student = findViewById(R.id.student);
        Button teacher = findViewById(R.id.teacher);
        Button industry = findViewById(R.id.industry);
        student.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), stud_regi.class);
            startActivity(intent);
        });

        teacher.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), teach_regi.class);
            startActivity(intent);
        });

        industry.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), indust_regi.class);
            startActivity(intent);
        });


        login.setOnClickListener(v -> {
            String email = lemail.getText().toString().trim();
            String pass = lpass.getText().toString();
            blur.setVisibility(View.VISIBLE);

            if(email.length()==0 && pass.length()==0){
                blur.setVisibility(View.GONE);
                Toast.makeText(this, "All Fields Are Required!!", Toast.LENGTH_SHORT).show();
            }else{
                fauth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(authResult -> fstore.collection("Industry").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.getDocuments().isEmpty()){
                        for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                            String i = document.getString("Email");
                            assert i != null;
                            if(i.contains(email)){
                                blur.setVisibility(View.INVISIBLE);
                                 Bundle bundle1 = new Bundle();
                                String type2 = "Industry";
                                bundle1.putString("value", type2);
                                Intent intent = new Intent(Login.this, Login.class);
                                intent.putExtras(bundle1);
                                startActivity(intent);
                                finish();
                            }else{
                                fstore.collection("Teacher").get().addOnSuccessListener(queryDocumentSnapshots12 -> {
                                    for(QueryDocumentSnapshot document12 : queryDocumentSnapshots12){

                                        String i12 = document12.getString("Email");
                                        assert i12 != null;
                                        if(i12.contains(email)){
                                            blur.setVisibility(View.INVISIBLE);
                                            Bundle bundle1 = new Bundle();
                                            String type2 = "Teacher";
                                            bundle1.putString("value", type2);
                                            Intent intent = new Intent(Login.this, Login.class);
                                            intent.putExtras(bundle1);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            fstore.collection("Student").get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                                                for(QueryDocumentSnapshot document1 : queryDocumentSnapshots1){

                                                    String i1 = document1.getString("Email");
                                                    assert i1 != null;
                                                    if(i1.contains(email)) {
                                                        blur.setVisibility(View.INVISIBLE);
                                                         Bundle bundle1 = new Bundle();
                                                        String type2 = "Student";
                                                        bundle1.putString("value", type2);
                                                        Intent intent = new Intent(Login.this, Login.class);
                                                        intent.putExtras(bundle1);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    }else{
                        blur.setVisibility(View.INVISIBLE);
                    }

                }).addOnFailureListener(e -> {
                    blur.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                })).addOnFailureListener(e -> {
                    blur.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "User Doesn't Exists!!", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}