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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.planpro.R;
import com.example.planpro.project.task.AddTask;
import com.example.planpro.project.task.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewProject extends AppCompatActivity {

    private ImageView back;
    private TextView projectNameTV, descriptionTV, startDateTV, endDateTV, totalCostTV;
    private RecyclerView tasksRecyclerView;
    private RecyclerView.LayoutManager Tasks_LayoutManager;
    private TaskAdapter Task_adapter;
    private dbSetUp DB = new dbSetUp();
    private String projectName, projectID, description, startDate, endDate;
    private double totalCost;
    ArrayList<Task> tasks = new ArrayList<Task>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_project);
        init();



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

        //call mathod
        getTasks();



        //set texts
        projectNameTV.setText(projectName);
        descriptionTV.setText(description);
        startDateTV.setText(startDate);
        endDateTV.setText(endDate);
        totalCostTV.setText(totalCost+"");



        // Add task Button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
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
        //find all views and textfields
        back = findViewById(R.id.backButton);
        projectNameTV = findViewById(R.id.projectName);
        descriptionTV = findViewById(R.id.description);
        startDateTV = findViewById(R.id.startDate);
        endDateTV = findViewById(R.id.endDate);
        totalCostTV = findViewById(R.id.totalCost);
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
    }

    private void getTasks(){
        //view tasks, query tasks from database by projectID
        //at the same time, calculate total cost of tasks
        totalCost = 0;
        com.google.android.gms.tasks.Task<QuerySnapshot> docRef = dbSetUp.db.collection("Task")
                .whereEqualTo("ProjectID", projectID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            tasks.clear();


                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final Task taskk = new Task();

                                String task_id = document.get("TaskID").toString();
                                String task_name = document.get("Name").toString();
                                Timestamp start_dateTS = (Timestamp)document.get("EarlyStartDate");
                                Timestamp end_dateTS = (Timestamp)document.get("EarlyFinishDate");

                                int task_cost = Integer.parseInt(document.get("TaskCost").toString());


                                taskk.setID(task_id);
                                taskk.setName(task_name);
                                taskk.setStart(start_dateTS);
                                taskk.setEnd(end_dateTS);
                                taskk.setCost(task_cost);

                                totalCost+=task_cost;

                                // Add to list
                                tasks.add(taskk);
                                Task_adapter.notifyDataSetChanged();
                            }

                            totalCostTV.setText(totalCost+"");



                        }//if
                    }// onComplete
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });//addOnCompleteListener

    }//getTasks
}
