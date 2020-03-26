package com.example.planpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity implements View.OnClickListener{


    Timer timer;
    boolean click = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();

        ConstraintLayout splashlin = findViewById(R.id.splash);
        splashlin.setOnClickListener(this);
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if (click)
                    return;

                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();

            }
        }, 2000);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.splash:

                    //User is logged in
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                break;
        }

    }

}
