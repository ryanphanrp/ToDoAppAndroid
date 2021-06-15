package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private final static int DELAY_DURATION = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        assert getSupportActionBar() != null;
        getSupportActionBar().hide();

        final Intent i = new Intent(SplashActivity.this, MainActivity.class);

        /*Set Delay Splash Screen*/
        // Replace new Runnable() by lambda
        new Handler().postDelayed(() -> {
            startActivity(i);
            finish();
        }, DELAY_DURATION);
    }
}