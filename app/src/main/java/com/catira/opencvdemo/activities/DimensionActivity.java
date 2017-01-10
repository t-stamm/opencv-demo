package com.catira.opencvdemo.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;

import com.catira.opencvdemo.R;

public class DimensionActivity extends AppCompatActivity {

ImageButton weiter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dimension);

        weiter = (ImageButton) findViewById(R.id.fa_button_dim);
        weiter.setOnClickListener(new View.OnClickListener() {

            public void onClick (View v) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            }
        });
    }

    // Laden der Menuressource
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dimension, menu);
        return true;
    }

}

