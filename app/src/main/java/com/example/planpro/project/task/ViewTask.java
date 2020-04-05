package com.example.planpro.project.task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.planpro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewTask extends AppCompatActivity {

    private ImageView back;
    private TextView taskNameTV, startDateTV, endDateTV, taskCostTV,resourcesTV;
    private String taskId, taskName, startD, endD, resources="";
    private String taskC;

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

        taskNameTV = findViewById(R.id.textView2);
        startDateTV = findViewById(R.id.Start);
        endDateTV = findViewById(R.id.End);
        resourcesTV = findViewById(R.id.Resources);
        taskCostTV = findViewById(R.id.TaskCost);

    }
}
