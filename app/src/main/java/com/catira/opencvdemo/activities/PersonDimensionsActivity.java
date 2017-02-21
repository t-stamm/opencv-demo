package com.catira.opencvdemo.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.catira.opencvdemo.R;
import com.catira.opencvdemo.services.BikeSizeCalculator;

public class PersonDimensionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_dimensions);

        String LOG_TAG = PersonDimensionsActivity.class.getSimpleName();

        BikeSizeCalculator objBikeSizeCalc = new BikeSizeCalculator();
        //getInsideLeg ist ne eigene Methode
        //String toastInfo = String.valueOf(objBikeSizeCalc.getFrameSize(getInsideLeg()));
        String toastInfoCrank = String.valueOf(objBikeSizeCalc.getSaddleHeight(getInsideLeg()));
        Log.i(LOG_TAG, "CrankLength: " + toastInfoCrank);

        String toastInfoOOL = String.valueOf(objBikeSizeCalc.getOOLength(getInsideLeg(),getTrunkLength(),getArmLength(),getSeatPosIndex()));
        Log.i(LOG_TAG, "Schrittlänge: " + getInsideLeg() +
                " Rumpflänge: " + getTrunkLength() +
                " Armlänge: " + getArmLength() +
                " Sitzpositions-Index H: " + getSeatPosIndex()
        );
        Log.i(LOG_TAG, "Oberrohrlänge: " + toastInfoOOL);

        Toast.makeText(this, "Sattelhöhe: " + toastInfoCrank,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_aktiendetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.preferences.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, EinstellungenActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getInsideLeg() {
        // Auslesen der ausgewählten Schrittlänge aus den SharedPreferences
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String prefSeatPosKey = getString(R.string.preferenceInsideLegKey);
        String prefSeatPosDefault = getString(R.string.preferenceInsideLegDefault);
        String valInsideLeg = sPrefs.getString(prefSeatPosKey, prefSeatPosDefault);
        return valInsideLeg;
    }
    public String getTrunkLength() {
        // Auslesen der ausgewählten Schrittlänge aus den SharedPreferences
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String prefTrunkKey = getString(R.string.preferenceTrunkLengthKey);
        String prefTrunkLengthDefault = getString(R.string.preferenceTrunkLengthDefault);
        String valTrunkLength = sPrefs.getString(prefTrunkKey, prefTrunkLengthDefault);
        return valTrunkLength;
    }
    public String getArmLength() {
        // Auslesen der ausgewählten Schrittlänge aus den SharedPreferences
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String prefArmLengthKey = getString(R.string.preferenceArmLengthKey);
        String prefArmLengthDefault = getString(R.string.preferenceArmLengthDefault);
        String valArmLength = sPrefs.getString(prefArmLengthKey, prefArmLengthDefault);
        return valArmLength;
    }
    public String getSeatPosIndex() {
        // Auslesen der ausgewählten Schrittlänge aus den SharedPreferences
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String prefSeatPosKey = getString(R.string.listSeatPosIndex);
        String prefSeatPosDefault = getString(R.string.listSeatPosIndexDefaultValue);
        String valSeatPos = sPrefs.getString(prefSeatPosKey, prefSeatPosDefault);
        return valSeatPos;
    }

    public void showSettings(View view) {
        // Auslesen der ausgewählten Aktienliste aus den SharedPreferences
        //SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        //String prefAktienlisteKey = getString(R.string.preference_aktienliste_key);
        //String prefAktienlisteDefault = getString(R.string.preference_aktienliste_default);
        //String aktienliste = sPrefs.getString(prefAktienlisteKey,prefAktienlisteDefault);

        // Hole ArrayListe listPref
        //String prefArrayKey = getString(R.string.listPref);
        //String prefArrayDefault = getString(R.string.listPrefDefaultValue);
        //String ArrayDefault = sPrefs.getString(prefArrayKey,prefArrayDefault);

        // Den Benutzer informieren, dass neue Aktiendaten im Hintergrund abgefragt werden
        //Toast.makeText(this, aktienliste, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, ArrayDefault, Toast.LENGTH_SHORT).show();

        BikeSizeCalculator objBikeSizeCalc = new BikeSizeCalculator();
        String toastInfo = String.valueOf(objBikeSizeCalc.getFrameSize(getInsideLeg()));
        Toast.makeText(this, "Rahmenhöhe: " + toastInfo +" cm", Toast.LENGTH_SHORT).show();

    }

}
