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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.catira.opencvdemo.R;


public class SettingActivity extends AppCompatActivity {

    ImageButton weiter;
    Button setPos;
    Button setZoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRunSet", true);
        if (isFirstRun) {
            // Place your dialog code here to display the dialog
            Fragment_Setting_Help fragment = new Fragment_Setting_Help();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(fragment, "Hilfe Reifengröße");
            transaction.commit();


            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRunSet", false)
                    .apply();
        }


        weiter = (ImageButton) findViewById(R.id.fa_button_set);
        weiter.setOnClickListener(new View.OnClickListener() {

            TextView positionEditText = (TextView) findViewById(R.id.set_pos);
            TextView wheelsizeEditText = (TextView) findViewById(R.id.set_zoll);

            public void onClick(View v) {

                if (positionEditText.getText().length() == 0 || wheelsizeEditText.getText().length() == 0) {
                    Toast toast = Toast.makeText(SettingActivity.this, getString(R.string.toast_setting), Toast.LENGTH_SHORT);
                    TextView v2 = (TextView) toast.getView().findViewById(android.R.id.message);
                    v2.setGravity(Gravity.CENTER);
                    toast.show();
                    return;
                } else {
                    //final Intent mContext = new Intent(getApplicationContext(), MeasurmentContext.class);
                    MeasurmentContext.currentSettingPos = positionEditText.getText().toString();
                    MeasurmentContext.currentSettingWheelSize = Integer.parseInt(wheelsizeEditText.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                    //intent.putExtra("wheelSize", wheelsizeEditText.getText());
                    startActivity(intent);
                }}
        });

        setPos = (Button) findViewById(R.id.choice_pos);
        setPos.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Fragment_Setting fragment = new Fragment_Setting();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.add(fragment, "Sitzposition auswählen...");
                transaction.commit();
            }
        });

        setZoll = (Button) findViewById(R.id.choice_zoll);
        setZoll.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Fragment_Wheelsize fragment = new Fragment_Wheelsize();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.add(fragment, "Reifengröße auswählen...");
                transaction.commit();
            }
        });
    }

    //Mit Help Menü Button Dialog starten über Fragment Manager
    public void doSet(MenuItem menuItem) {
        Fragment_Setting_Help fragment = new Fragment_Setting_Help();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(fragment, "Hilfe Reifengröße");
        transaction.commit();
    }

    // Laden der Menuressource
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_setting, menu);
        return true;
    }
}




