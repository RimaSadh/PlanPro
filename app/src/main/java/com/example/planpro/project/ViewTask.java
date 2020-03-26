package com.example.planpro.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.planpro.R;

public class ViewTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        getSupportActionBar().hide();
    }
}
