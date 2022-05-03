package com.example.projectsubmitter.submission;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectsubmitter.Login;
import com.example.projectsubmitter.R;
import com.example.projectsubmitter.classroom.cls_room;
import com.example.projectsubmitter.helper.UploadListAdapter;
import com.example.projectsubmitter.profile.stud_profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class submit2 extends AppCompatActivity {

    private ImageButton mSelectBtn;
    private RecyclerView mUploadList;

    FirebaseAuth fauth;
    FirebaseFirestore fs;
    private List<String> fileNameList;
    private List<String> fileDoneList;
    Uri fileUri;
    String title, roomcode, stid, useruid, date, cdate, mod;
    private StorageReference mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit2);
        TextView ctitle = findViewById(R.id.main);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("title");
            roomcode = bundle.getString("roomcode");
            date = bundle.getString("date");
            ctitle.setText(title);
        }

        fauth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        if(fauth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), Login.class));
        }else{
            useruid = fauth.getCurrentUser().getUid();
        }


        DocumentReference refer = fs.collection("Student").document(useruid);
        refer.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    stid =  task.getResult().getString("ID");
                }
            }
        });

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        cdate = simpleDateFormat.format(c);

        mStorage = FirebaseStorage.getInstance().getReference();

        mSelectBtn = (ImageButton) findViewById(R.id.select_btn);
        mUploadList = (RecyclerView) findViewById(R.id.upload_list);

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();

        mSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (date.compareTo(cdate) < 0) {
                    mod = "Late Submission";
                }else{
                    mod = "Submission On Time";
                }

                Intent i = new Intent();
                i.setType("*/*");
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                i.setAction(Intent.ACTION_GET_CONTENT);
                someActivityResultLauncher.launch(i);
            }
        });
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Map<String, Object> ata = new HashMap<>();
                        ata.put("null", "null");

                        Intent data = result.getData();
                            if(data.getClipData() != null){
                                int totalItemsSelected = data.getClipData().getItemCount();
                                for(int i = 0; i < totalItemsSelected; i++) {
                                    fileUri = data.getClipData().getItemAt(i).getUri();
                                    String fileName = getFileName(fileUri);
                                    fileNameList.add(fileName);
                                    fileDoneList.add("uploading");
                                    StorageReference fileToUpload = mStorage.child("Submission").child(roomcode).child(title).child(stid).child(fileName);
                                    final int finalI = i;
                                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            fileDoneList.remove(finalI);
                                            fileDoneList.add(finalI, "done");

                                        }
                                    });
                                }
                                Map<String, Object> docData = new HashMap<>();
                                docData.put("Mode", mod);
                                docData.put("Submission Date", cdate);
                                DocumentReference q = fs.collection("Submission").document(roomcode);
                                q.set(ata);
                                DocumentReference w = q.collection("Problems").document(title);
                                w.set(ata);
                                w.collection("Student Id").document(stid).set(docData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(submit2.this, "Submitted", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }else if (data.getData() != null){
                                fileUri = data.getData();
                                String fileName = getFileName(fileUri);
                                fileNameList.add(fileName);
                                fileDoneList.add("uploading");
                                StorageReference fileToUpload = mStorage.child("Submission").child(roomcode).child(title).child(stid).child(fileName);
                                final int finalI = 0;
                                fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        fileDoneList.remove(finalI);
                                        fileDoneList.add(finalI, "done");

                                    }
                                });

                                Map<String, Object> docData = new HashMap<>();
                                docData.put("Mode", mod);
                                docData.put("Submission Date", cdate);
                                DocumentReference q = fs.collection("Submission").document(roomcode);
                                q.set(ata);
                                DocumentReference w = q.collection("Problems").document(title);
                                w.set(ata);
                                w.collection("Student Id").document(stid).set(docData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(submit2.this, "Submitted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                });

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}