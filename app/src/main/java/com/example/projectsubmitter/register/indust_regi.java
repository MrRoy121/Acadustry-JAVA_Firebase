package com.example.projectsubmitter.register;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.projectsubmitter.Login;
import com.example.projectsubmitter.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class indust_regi extends AppCompatActivity {

    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String ename, eemail, epass, efield;
    String str = "Industry";
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String type = "type";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indust_regi);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        sp = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        RelativeLayout blur = findViewById(R.id.blur);
        EditText rname = findViewById(R.id.rname);
        EditText remail = findViewById(R.id.remail);
        EditText rfield = findViewById(R.id.rfield);
        EditText rpass = findViewById(R.id.rpass);
        CheckBox cbox = findViewById(R.id.checkBox1);

        Button register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbox.isChecked()) {

                    blur.setVisibility(View.VISIBLE);
                    efield = rfield.getText().toString().trim();
                    ename = rname.getText().toString().trim();
                    eemail = remail.getText().toString().trim();
                    epass = rpass.getText().toString().trim();
                    fauth.createUserWithEmailAndPassword(eemail, epass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            String userid = fauth.getCurrentUser().getUid();
                            DocumentReference refer = fstore.collection(str).document(userid);
                            Map<String, Object> user = new HashMap<>();
                            user.put("Email", eemail);
                            user.put("Name", ename);
                            user.put("Field", efield);
                            user.put("Password", epass);
                            refer.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    blur.setVisibility(View.INVISIBLE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString(type, str);
                                    editor.apply();
                                    Toast.makeText(getApplicationContext(),"Register Successful",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), Login.class));
                                    finish();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Register unsuccessful \n " + e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });

                }else
                    Toast.makeText(getApplicationContext(),"Please agree With our terms and conditions",Toast.LENGTH_SHORT).show();

            }
        });

    }
}