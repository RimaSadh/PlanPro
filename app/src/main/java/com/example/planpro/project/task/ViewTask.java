package com.example.planpro.project.task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planpro.R;
import com.example.planpro.project.ViewProject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewTask extends AppCompatActivity {

    private ImageView back;
    private TextView taskNameTV, startDateTV, endDateTV, taskCostTV,resourcesTV;
    private String taskId, taskName, startD, endD, resources="";
    private String taskC;
    private Button delete;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        getSupportActionBar().hide();

        init();

        // get extras
        taskId = getIntent().getStringExtra("task_id");
        taskName = getIntent().getStringExtra("task_name");
        startD = getIntent().getStringExtra("start_date");
        endD = getIntent().getStringExtra("end_date");
        taskC = getIntent().getStringExtra("task_cost");

        getResource();

        taskNameTV.setText(taskName);
        startDateTV.setText(startD);
        endDateTV.setText(endD);
        taskCostTV.setText(taskC);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(ViewTask.this);
                dialog.setTitle("Delete Task");
                dialog.setMessage("Are you sure you want to delete this task ? ");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("Tasks")
                                .document(taskId)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if(document.exists()){
                                                DocumentReference d= document.getReference();
                                                        d.delete();

                                                    }


                                            Toast.makeText(ViewTask.this, "Task deleted",
                                                    Toast.LENGTH_SHORT).show();

                                            finish();
                                                }



                                        else {
                                            Toast.makeText(ViewTask.this, task.getException().getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }); //end dialog onComplete
                    }



                });// end dialog onclick

                dialog.setNegativeButton("Cancel",null);

                AlertDialog alertDialog =  dialog.create();
                alertDialog.show();

            }
        });


    }

    private void getResource() {

        db.collection("Resource")
                .whereEqualTo("taskID",taskId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String resourceN = document.get("name").toString();
                                String resourceC = document.get("cost").toString();

                                resources += "- " + resourceN + " | " + resourceC + " $"+" \n";

                            }
                            resourcesTV.setText(resources);
                    }
                });

    }

    private void init() {
        back = findViewById(R.id.backButton);
        delete = findViewById(R.id.Delete);
        taskNameTV = findViewById(R.id.textView2);
        startDateTV = findViewById(R.id.Start);
        endDateTV = findViewById(R.id.End);
        resourcesTV = findViewById(R.id.Resources);
        taskCostTV = findViewById(R.id.TaskCost);

    }
}
