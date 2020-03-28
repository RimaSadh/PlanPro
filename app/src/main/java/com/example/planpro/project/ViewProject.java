package com.example.planpro.project;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.planpro.R;
import com.example.planpro.project.task.AddTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ViewProject extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_project);

        // Add Button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                startActivity(new Intent(getApplicationContext(), AddTask.class));
            }
        });

        getSupportActionBar().hide();
    }
}
