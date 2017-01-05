package com.catira.opencvdemo.activities;

/**
 * Created by ck on 19.12.2016.
 */

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import com.catira.opencvdemo.R;

public class EinstellungenActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
/*
        Preference aktienlistePref_1 = findPreference(getString(R.string.preference_aktienliste_key));
        aktienlistePref_1.setOnPreferenceChangeListener(this);

        // onPreferenceChange sofort aufrufen mit der in SharedPreferences gespeicherten Aktienliste
        SharedPreferences sharedPrefs_1 = PreferenceManager.getDefaultSharedPreferences(this);
        String gespeicherteAktienliste_1 = sharedPrefs_1.getString(aktienlistePref_1.getKey(), "");
        onPreferenceChange(aktienlistePref_1, gespeicherteAktienliste_1);


        Preference aktienlistePref_2 = findPreference(getString(R.string.preferenceSitzpositionKey));
        aktienlistePref_2.setOnPreferenceChangeListener(this);

        SharedPreferences sharedPrefs_2 = PreferenceManager.getDefaultSharedPreferences(this);
        String gespeicherteAktienliste_2 = sharedPrefs_2.getString(aktienlistePref_2.getKey(), "");
        onPreferenceChange(aktienlistePref_2, gespeicherteAktienliste_2);
*/
        // Get a reference to the preferences
        // the next two lines for preference group
        // http://stackoverflow.com/questions/4966816/how-to-create-radiobutton-group-in-preference-xml-window
        ListPreference mListPreference = (ListPreference)getPreferenceScreen().findPreference(getString(R.string.listSeatPosIndex));

        Toast.makeText(this, "Einstellungen-Activity gestartet.", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Zurück mit Back-Button.", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, mListPreference.getEntry() , Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {

        preference.setSummary(value.toString());

        return true;
    }
}