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

import com.catira.opencvdemo.R;

public class KickoffActivity extends AppCompatActivity {
    private Toast toast;
    private long lastBackPressTime = 0;

    ImageButton floatButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kickoff);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setLogo(R.drawable.ab_app_logo);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowHomeEnabled(true);



        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRunKick", true);
        if (isFirstRun) {
            // Place your dialog code here to display the dialog
            Fragment_Help fragment = new Fragment_Help();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(fragment, "Hilfe");
            transaction.commit();

            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRunKick", false)
                    .apply();
        }

        Fragment_Kickoff_List fragment = new Fragment_Kickoff_List();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(fragment, "Kickoff");
        transaction.commit();

        floatButton = (ImageButton) findViewById(R.id.fa_button);
        floatButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DimensionActivity.class);
                startActivity(intent);
            }
        });
    }




    //Mit Info Menü Button Dialog starten über Fragment Manager
    public void doInfo(MenuItem menuItem) {
        Fragment_Impressum fragment = new Fragment_Impressum();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(fragment, "Impressum");
        transaction.commit();
    }

    //Mit Help Menü Button Dialog starten über Fragment Manager
    public void doHelp(MenuItem menuItem) {
        Fragment_Help fragment = new Fragment_Help();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(fragment, "Hilfe");
        transaction.commit();
    }

    // Laden der Menuressource
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_kickoff, menu);
        return true;
    }

    //App schließen bei 2x Backpress
    @Override
    public void onBackPressed() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this, getString(R.string.toast_backpress_dimension_activity), Toast.LENGTH_LONG);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            if (v != null) v.setGravity(Gravity.CENTER); //Text zentriert im Toast anzeigen
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                toast.cancel();
            }
            super.onBackPressed();

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
           /* intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); Was macht setFlags  */
            startActivity(intent);
            finish();
            System.exit(0);
        }
    }
}
