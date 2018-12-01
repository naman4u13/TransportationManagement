package com.example.divyanshsingh.transportationmanagement.acitivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.divyanshsingh.transportationmanagement.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Thread background = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    Intent intent;

                    intent = new Intent(SplashActivity.this, SearchVehicleActivity.class);
                    startActivity(intent);

                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        background.start();
    }
}


