package com.example.projectsubmitter.submission;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectsubmitter.R;
import com.example.projectsubmitter.profile.stud_profile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class stud_list_submission_adapter extends RecyclerView.Adapter<stud_list_submission_adapter.ViewHolder>{
    private ArrayList<my_submission_list> listdata;
    private Context context;
    ProgressDialog pd;

    // RecyclerView recyclerView;
    public stud_list_submission_adapter(ArrayList<my_submission_list> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }
    @Override
    public stud_list_submission_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.std_list, parent, false);
        stud_list_submission_adapter.ViewHolder viewHolder = new stud_list_submission_adapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(stud_list_submission_adapter.ViewHolder holder, int position) {
        final my_submission_list myListData = listdata.get(position);
        holder.name.setText(listdata.get(position).getDate());
        holder.d.setText(listdata.get(position).getId());
        holder.m.setText(listdata.get(position).getTime());
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        FirebaseStorage storagec = FirebaseStorage.getInstance();
        final StorageReference storageRef1 = storagec.getReference();
        pd = new ProgressDialog(context);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = myListData.getCode();
                String title = myListData.getTitle();
                String ids = myListData.getDate();


                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setGravity(Gravity.CENTER_HORIZONTAL);
                final EditText marke = new EditText(context);
                marke.setSingleLine(true);
                layout.setPadding(40, 20, 70, 0);
                marke.setHint("Mark Out of (/100)");
                marke.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(marke);
                final EditText come = new EditText(context);
                come.setHint("Give A Comment or Suggestion");
                come.setInputType(InputType.TYPE_CLASS_TEXT);
                layout.addView(come);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(layout);
                TextView dialogTitle = new TextView(context);
                dialogTitle.setText("Valuation For The Submission");
                dialogTitle.setPadding(30, 10, 60, 10);
                dialogTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
                builder.setCustomTitle(dialogTitle);
                builder.setMessage("Of Student Id : " + ids);

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(!(marke.getText().length() == 0) && !(come.getText().length() ==0)){

                            Map<String, Object> mark = new HashMap<>();
                            mark.put("Mark", marke.getText().toString());
                            mark.put("Teacher Comment", come.getText().toString());

                            fs.collection("Submission").document(code).collection("Problems").document(title).collection("Student Id")
                                    .document(ids).set(mark, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Marks And Comments Are Added Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else
                            Toast.makeText(context, "Please Provide Marks And Comments", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Download", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StorageReference s = storageRef1.child("Submission/"+ code +"/" + title +"/"+ ids);
                        s.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                            @Override
                            public void onSuccess(ListResult listResult) {
                                if(listResult != null){
                                    for (StorageReference item : listResult.getItems()) {
                                        Log.e("tag", String.valueOf(item));
                                        StorageReference sf = storagec.getReferenceFromUrl(String.valueOf(item));
                                        pd.setTitle("Downloading Please Wait!");
                                        pd.setIndeterminate(true);
                                        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                        pd.show();
                                        downloadfile(item,code,title,ids, sf.getName());
                                    }
                                }
                            }
                        });
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,d, m;
        public ImageView i;
        public CardView relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.d = itemView.findViewById(R.id.d);
            this.m = itemView.findViewById(R.id.m);
            this.name = itemView.findViewById(R.id.upload_filename);
            this.i = itemView.findViewById(R.id.upload_icon);
            this.relativeLayout = itemView.findViewById(R.id.cview);
        }
    }
    public void clear() {
        int size = listdata.size();
        listdata.clear();
        notifyItemRangeRemoved(0, size);
    }


    private void downloadfile(StorageReference down, String code, String title, String id, String fname) {

        final File rootPath = new File(Environment.getExternalStorageDirectory(), code+"_"+title+"_"+id);

        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }
        final File localFile = new File(rootPath, fname);

        if(!pd.isShowing()){
            pd.show();
        }
        down.getFile(localFile).addOnSuccessListener(new OnSuccessListener <FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                pd.dismiss();
                Toast.makeText(context, "Download Incompleted", Toast.LENGTH_LONG).show();
            }
        });
    }
}