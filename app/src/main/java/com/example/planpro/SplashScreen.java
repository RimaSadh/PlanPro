package com.example.planpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity implements View.OnClickListener{

    Timer timer;
    boolean click = false;
    private FirebaseAuth mAuth;
    int step = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


//        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
//        step = sharedPref.getInt(Constants.Keys.REGISTRATION_STEP, 1);


        ConstraintLayout splashlin = findViewById(R.id.splash);
        splashlin.setOnClickListener(this);
       // mAuth = FirebaseAuth.getInstance();
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if (click)
                    return;

                //user is logged in
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
