package com.catira.opencvdemo.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.catira.opencvdemo.BuildConfig;
import com.catira.opencvdemo.R;
import com.catira.opencvdemo.model.MeasurementContext;
import com.catira.opencvdemo.model.PersonDimensions;

public class DimensionActivity extends AppCompatActivity {

    ImageButton weiter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dimension);

        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRunDim", true);
        if (isFirstRun) {
            // Place your dialog code here to display the dialog
            Fragment_Dimension fragment = new Fragment_Dimension();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(fragment, "Dimension");
            transaction.commit();


            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRunDim", false)
                    .apply();
        }

        weiter = (ImageButton) findViewById(R.id.fa_button_dim);
        weiter.setOnClickListener(new View.OnClickListener() {

            TextView lengthEditText1 = (TextView) findViewById(R.id.arm_length);
            TextView lengthEditText2 = (TextView) findViewById(R.id.inside_leg_length);
            TextView lengthEditText3 = (TextView) findViewById(R.id.body_length);

            public void onClick(View v) {

                if (lengthEditText1.getText().length() == 0 || lengthEditText2.getText().length() == 0 || lengthEditText3.getText().length() == 0) {
                    Toast toast = Toast.makeText(DimensionActivity.this, getString(R.string.toast_dimension), Toast.LENGTH_SHORT);
                    TextView v2 = (TextView) toast.getView().findViewById(android.R.id.message);
                    v2.setGravity(Gravity.CENTER);
                    toast.show();
                    return;
                } else {
                    PersonDimensions person = new PersonDimensions(
                            Double.parseDouble(lengthEditText1.getText().toString()),
                            Double.parseDouble(lengthEditText2.getText().toString()),
                            Double.parseDouble(lengthEditText3.getText().toString())
                    );
                    final Intent mContext = new Intent(getApplicationContext(), MeasurementContext.class);
                    MeasurementContext.currentPersDimen = person;
                    Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(intent);
                }
            }
        });

        if (BuildConfig.DEBUG) {
            ((TextView) findViewById(R.id.arm_length)).setText("80");
            ((TextView) findViewById(R.id.inside_leg_length)).setText("90");
            ((TextView) findViewById(R.id.body_length)).setText("60");
        }
    }

    // Laden der Menuressource
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dimension, menu);
        return true;
    }

    //Mit Info Menü Button Dialog starten über Fragment Manager
    public void doHuman(MenuItem menuItem) {
        Fragment_Dimension fragment = new Fragment_Dimension();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(fragment, "Dimension");
        transaction.commit();
    }

}