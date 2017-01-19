package com.catira.opencvdemo.services;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;
import java.util.concurrent.ThreadFactory;

import com.catira.opencvdemo.R;
import com.catira.opencvdemo.model.BikeCalculationHistoryEntry;

import org.json.JSONArray;

/**
 * Created by Timo on 26.10.2016.
 */

public class BikeCalculationHistoryStorage {

    private static String BIKE_HISTORY_SETTINGS = "bike_calculation_history";
    private final SharedPreferences mSharedPref;

    private List<BikeCalculationHistoryEntry> entries;

    public BikeCalculationHistoryStorage(Context context) {
        this.mSharedPref = context.getSharedPreferences(BIKE_HISTORY_SETTINGS, Context.MODE_PRIVATE);
        load();
    }

    private void load() {
        // TODO implement
    }

    public void addEntry(BikeCalculationHistoryEntry entry) {
        entries.add(entry);
        save();
    }

    public List<BikeCalculationHistoryEntry> getEntries() {
        return entries;
    }

    public void save() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                saveData();
            }
        }).run();
    }

    private void saveData() {
        SharedPreferences.Editor editor = this.mSharedPref.edit();
        JSONArray array = new JSONArray();
        for(BikeCalculationHistoryEntry entry : entries) {
            array.put(entry.getJson());
        }
        editor.putString(BIKE_HISTORY_SETTINGS, array.toString());
        editor.commit();
    }
}
