package com.example.planpro.project;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planpro.dbSetUp;
import com.example.planpro.project.task.TaskAdapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planpro.R;
import com.example.planpro.project.task.AddTask;
import com.example.planpro.project.task.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewProject extends AppCompatActivity {

    private ImageView back;
    private TextView projectNameTV, descriptionTV, startDateTV, endDateTV, totalCostTV, noTasks;
    private RecyclerView tasksRecyclerView;
    private RecyclerView.LayoutManager Tasks_LayoutManager;
    private TaskAdapter Task_adapter;
    private String projectName, projectID, description, startDate, endDate;
    private double totalCost, resourceCost, taskCost;
    ArrayList<Task> tasks = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


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


        //view tasks, query tasks from database by projectID
        Tasks_LayoutManager = new LinearLayoutManager(this);
        tasksRecyclerView.setLayoutManager( Tasks_LayoutManager );
        Task_adapter = new TaskAdapter(this, tasks);
        tasksRecyclerView.setAdapter(Task_adapter);

        //call method
        getTasks();

        //set texts
        projectNameTV.setText(projectName);
        descriptionTV.setText(description);
        startDateTV.setText(startDate);
        endDateTV.setText(endDate);
        totalCostTV.setText(totalCost+"");


        // Add task Button
        Button fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddTask.class);
                intent.putExtra("project_id", projectID);
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

    }

    private void init(){
        //find all views and text fields
        back = findViewById(R.id.backButton);
        projectNameTV = findViewById(R.id.projectName);
        descriptionTV = findViewById(R.id.description);
        startDateTV = findViewById(R.id.startDate);
        endDateTV = findViewById(R.id.endDate);
        totalCostTV = findViewById(R.id.totalCost);
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        noTasks = findViewById(R.id.noTask);
        noTasks.setVisibility(View.GONE);
    }

    private void getTasks(){
        //view tasks, query tasks from database by projectID
        //at the same time, calculate total cost of tasks

        db.collection("Tasks")
                .whereEqualTo("ProjectID", projectID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

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


                                taskCost += task_cost;
                                calculateCost(task_id,taskCost);

                                // Add to list
                                tasks.add(taskk);
                                Task_adapter.notifyDataSetChanged();

                            }// end for

                                noTasks.setVisibility(View.GONE);

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


    public void calculateCost(String taskID, final double taskCost){

     db.collection("Resource")
             .whereEqualTo("taskID",taskID)
             .get()
             .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                 @Override
                 public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                     if (task.isSuccessful()) {

                             for (QueryDocumentSnapshot document : task.getResult()) {

                                     int resource = Integer.parseInt(document.get("cost").toString());
                                     totalCost += resource;
                                     //totalCost = resourceCost + taskCost;

                             }
                         totalCostTV.setText(totalCost+"");

                     }
                 }
             });

    }

    @Override
    protected void onResume() {
        super.onResume();
        tasks.clear();
        getTasks();

    }
}
