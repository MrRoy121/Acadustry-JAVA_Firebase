package com.example.projectsubmitter.submission;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectsubmitter.R;
import com.example.projectsubmitter.helper.room_stud_list;
import com.example.projectsubmitter.helper.room_student_list_adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class my_submission_list_adapter extends RecyclerView.Adapter<my_submission_list_adapter.ViewHolder>{
    private my_submission_list[] listdata;
    private Context context;
    FirebaseFirestore fs;


    public my_submission_list_adapter(ArrayList<my_submission_list> listdata, Context context) {
        this.listdata = listdata.toArray(new my_submission_list[0]);
        this.context = context;
    }
    @Override
    public my_submission_list_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.submission_list, parent, false);
        my_submission_list_adapter.ViewHolder viewHolder = new my_submission_list_adapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(my_submission_list_adapter.ViewHolder holder, int position) {
        final my_submission_list myListData = listdata[position];
        holder.title.setText(listdata[position].getTitle());
        holder.tname.setText(listdata[position].getTime());
        holder.section.setText(listdata[position].getCode());
        holder.subject.setText(listdata[position].getDate());
        final String[] mark = new String[1];
        final String[] rating = new String[1];
        final String[] tcom = new String[1];
        final String[] icom = new String[1];
        mark[0] = "Not Givven Yet";
        tcom[0] = "Not Givven Yet";
        rating[0] = "Not Givven Yet";
        icom[0] = "Not Givven Yet";
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fs = FirebaseFirestore.getInstance();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                TextView dialogTitle = new TextView(context);
                dialogTitle.setText("Marks And Comments");
                dialogTitle.setPadding(30, 10, 60, 10);
                dialogTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
                builder.setCustomTitle(dialogTitle);
                builder.setMessage("For Project "+myListData.getTitle());
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View customLayout = inflater.inflate(R.layout.alert_sub, null);
                builder.setView(customLayout);
                TextView mar = customLayout.findViewById(R.id.mark);
                TextView tc = customLayout.findViewById(R.id.tcom);
                TextView rat = customLayout.findViewById(R.id.rate);
                TextView ic = customLayout.findViewById(R.id.icom);
                fs.collection("Submission").document(myListData.getCode()).collection("Problems").document(myListData.getTitle()).collection("Student Id").document(myListData.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> taskcx) {
                        if (taskcx.isSuccessful()) {
                            if (!(Objects.equals(taskcx.getResult().get("Mark"), null))){
                                mark[0] = String.valueOf(taskcx.getResult().get("Mark"));
                                tcom[0] = String.valueOf(taskcx.getResult().get("Teacher Comment"));
                            }
                            if (!(Objects.equals(taskcx.getResult().get("Rating"), null))){
                                rating[0] = String.valueOf(taskcx.getResult().get("Rating"));
                                icom[0] = String.valueOf(taskcx.getResult().get("Industry Comment"));
                            }
                            mar.setText(mark[0]);
                            tc.setText(tcom[0]);
                            rat.setText(rating[0]);
                            ic.setText(icom[0]);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });


            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title,tname,section,subject;
        public CardView relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.ptle);
            this.tname= (TextView) itemView.findViewById(R.id.date);
            this.section = (TextView) itemView.findViewById(R.id.code);
            this.subject = (TextView) itemView.findViewById(R.id.time);
            this.relativeLayout = (CardView) itemView.findViewById(R.id.layut);
        }
    }
}