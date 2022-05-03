package com.example.projectsubmitter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.projectsubmitter.profile.indust_profile;
import com.example.projectsubmitter.profile.stud_profile;
import com.example.projectsubmitter.profile.teach_profile;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth fauth;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String type = "type";
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fauth = FirebaseAuth.getInstance();
        sp = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String value = sp.getString("type", "");
        if(fauth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), Login.class));
        }else  if (value.equals("Student")){
            startActivity(new Intent(getApplicationContext(), stud_profile.class));
        }else if (value.equals("Teacher")){
            startActivity(new Intent(getApplicationContext(), teach_profile.class));
        }else if (value.equals("Industry")) {
            startActivity(new Intent(getApplicationContext(), indust_profile.class));
        }else
            startActivity(new Intent(getApplicationContext(), Login.class));
    }
}