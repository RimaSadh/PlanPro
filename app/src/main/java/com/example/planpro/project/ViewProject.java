package com.example.planpro.project;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planpro.project.task.TaskAdapter;

import android.annotation.SuppressLint;
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
import com.example.planpro.project.task.AddTask;
import com.example.planpro.project.task.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewProject extends AppCompatActivity {

    private ImageView back;
    private TextView projectNameTV, descriptionTV, startDateTV, endDateTV, lateEndDateTV, totalCostTV, noTasks;
    private RecyclerView tasksRecyclerView;
    private RecyclerView.LayoutManager Tasks_LayoutManager;
    private TaskAdapter Task_adapter;
    private Button delete;
    private String projectName, projectID, description, startDate, endDate, taskID, LateDate;
    private double totalCost, resourceCost, taskCost;
    ArrayList<Task> tasks = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private double totalC=0;
    private boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_project);

        init();

        totalCost = 0;
        taskCost = 0;
        resourceCost = 0;


        //get string intents
        projectID = getIntent().getStringExtra("project_id");
        projectName = getIntent().getStringExtra("project_name");
        description = getIntent().getStringExtra("projects_description");
        startDate = getIntent().getStringExtra("start_date");
        endDate = getIntent().getStringExtra("end_date");
        LateDate = getIntent().getStringExtra("late_end");

        //for test can't send the extra for intent //Don't removed just update the value
        if ( projectID == null ) {
            projectID ="KEMUHdFjORgcUxujCT64";
        }
        if ( projectName == null ) {
            projectName ="GP";
        }
        if ( description == null ) {
            description ="This Project Of Graduation Project For Second Term";
        }
        if ( startDate == null ) {
            startDate ="Sun,2 Aug 2020";
        }
        if ( endDate == null ) {
            endDate ="Fri,2 Oct 2020";
        }
        if ( LateDate == null ) {
            LateDate ="Fri,1 Jan 2021";
        }


        //view tasks, query tasks from database by projectID
        Tasks_LayoutManager = new LinearLayoutManager(this);
        tasksRecyclerView.setLayoutManager( Tasks_LayoutManager );
        Task_adapter = new TaskAdapter(this, tasks);
        tasksRecyclerView.setAdapter(Task_adapter);

        totalC=0;
        first = true;
        //call method
        getTasks();
        
        //set texts
        projectNameTV.setText(projectName);
        descriptionTV.setText(description);
        startDateTV.setText(startDate);
        endDateTV.setText(endDate);
        lateEndDateTV.setText(LateDate);


        // Add task Button
        Button fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddTask.class);
                intent.putExtra("project_id", projectID);
                intent.putExtra("start_Date", startDate);
                intent.putExtra("end_Date", endDate);
                intent.putExtra("late_end", LateDate);
                startActivity(intent);
            }
        });

        getSupportActionBar().hide();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(ViewProject.this);
                dialog.setTitle("Delete Project");
                dialog.setMessage("Are you sure you want to delete this project ? ");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("Projects")
                                .document(projectID)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if(document.exists()){
                                                DocumentReference d = document.getReference();
                                                d.delete();

                                            }


                                            Toast.makeText(ViewProject.this, "Project deleted",
                                                    Toast.LENGTH_LONG).show();
                                            finish();
                                        }



                                        else {
                                            Toast.makeText(ViewProject.this, task.getException().getMessage(),
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

    private void init(){
        //find all views and text fields
        back = findViewById(R.id.backButton);
        projectNameTV = findViewById(R.id.projectName);
        descriptionTV = findViewById(R.id.description);
        startDateTV = findViewById(R.id.startDate);
        endDateTV = findViewById(R.id.endDate);
        lateEndDateTV = findViewById(R.id.lateEndDate);
        totalCostTV = findViewById(R.id.totalCost);
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        noTasks = findViewById(R.id.noTask);
        noTasks.setVisibility(View.GONE);
        delete = findViewById(R.id.Delete);
    }

    private void getTasks(){
        //view tasks, query tasks from database by projectID
        //at the same time, calculate total cost of tasks
        totalC=0;

        db.collection("Tasks")
                .whereEqualTo("ProjectID", projectID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            totalC= 0;

                            tasks.clear();

                            if(!task.getResult().isEmpty()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final Task taskk = new Task();

                                String task_id = document.getId();
                                String task_name = document.get("Name").toString();
                                Timestamp start_dateTS = (Timestamp)document.get("EarlyStartDate");
                                Timestamp end_dateTS = (Timestamp)document.get("EarlyFinishDate");

                                double task_cost = Double.parseDouble(document.get("TaskCost").toString());

                                taskk.setID(task_id);
                                taskk.setName(task_name);
                                taskk.setStart(start_dateTS);
                                taskk.setEnd(end_dateTS);
                                taskk.setCost(task_cost);

                                // Add to list
                                tasks.add(taskk);
                                Task_adapter.notifyDataSetChanged();

                                if(first)
                                calcTc(task_id, task_cost);

                            }// end for

                                noTasks.setVisibility(View.GONE);
                                first = false;
                        }}//if
                        if(tasks.size() == 0)
                            noTasks.setVisibility(View.VISIBLE);

                    }// onComplete
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });//addOnCompleteListener

    }


    public void calcTc(String Tid, final double task_cost){

        db.collection("Resource")
                .whereEqualTo("taskID",Tid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()){
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    double resource = Double.parseDouble(document.get("cost").toString());
                                    totalC += resource;

                                }

                                totalC += task_cost;
                                totalCostTV.setText(totalC+"");

                            }
                        }
                    }
                });
    }


//    public void calculateCost(){
//
//        db.collection("Tasks")
//                .whereEqualTo("ProjectID", projectID).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @SuppressLint("SetTextI18n")
//                    @Override
//                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
//
//                        final double costs [] = new double[1000];
//
//                        int i = 0;
//
//                         double totalCostCost=0;
//                        if(task.isSuccessful()){
//
//                            tasks.clear();
//
//                            if(!task.getResult().isEmpty()){
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                    taskID = document.getId();
//                                    double task_cost = Double.parseDouble(document.get("TaskCost").toString());
//                                    taskCost += task_cost;
//                                    db.collection("Resource")
//                                            .whereEqualTo("taskID",taskID)
//                                            .get()
//                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                @Override
//                                                public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
//                                                    if (task.isSuccessful()) {
//                                                        if(!task.getResult().isEmpty()){
//                                                            for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                                                double resource = Double.parseDouble(document.get("cost").toString());
//                                                                resourceCost += resource;
//
//
//                                                            }
//                                                        }
//
//
//
//                                                    }
//                                                }
//                                            });
//
//                                    totalCost += resourceCost + taskCost;
//                                    costs[i] = totalCost;
//                                    i++;
//
//                                }// end for
//
//
//                            }}
//                        for(int j=0; j<costs.length; j++){
//                            totalCostCost += costs[j];
//                        }
//                        totalCostTV.setText( totalCostCost+"");
//
//                        //i
//
//                    }// onComplete
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });//addOnCompleteListener
//
//
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
        tasks.clear();
        first = true;
        getTasks();
    }
}
