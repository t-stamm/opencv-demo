package com.catira.opencvdemo.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.catira.opencvdemo.R;
import com.catira.opencvdemo.services.BikeCalculationHistoryStorage;


public class ResultActivity extends AppCompatActivity implements Runnable {

    ImageButton saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        saveBtn = (ImageButton) findViewById(R.id.fa_button_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}