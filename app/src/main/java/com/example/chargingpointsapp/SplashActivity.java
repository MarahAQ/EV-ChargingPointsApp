package com.example.chargingpointsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isTaskRoot()) {
            finish();
            return;
        }

        setContentView(R.layout.activity_splash);
// Delay for 3 seconds (3000 milliseconds) before navigating to MainActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // ✅ Ensure SplashActivity is removed from the activity stack
        }, 3000);
    }
}