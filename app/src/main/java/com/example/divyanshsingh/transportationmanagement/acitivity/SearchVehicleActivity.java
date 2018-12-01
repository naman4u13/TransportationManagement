package com.example.divyanshsingh.transportationmanagement.acitivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.divyanshsingh.transportationmanagement.R;

public class SearchVehicleActivity extends AppCompatActivity {

    Button searchBuses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_vehicle);
        searchBuses = findViewById(R.id.search_bus);
        searchBuses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchVehicleActivity.this,DashboardActivity.class);
                startActivity(intent);
            }
        });
    }
}
