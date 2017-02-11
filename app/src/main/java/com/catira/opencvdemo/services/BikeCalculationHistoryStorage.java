package com.catira.opencvdemo.services;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

import com.catira.opencvdemo.App;
import com.catira.opencvdemo.R;
import com.catira.opencvdemo.model.BikeCalculationHistoryEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Timo on 26.10.2016.
 */

public class BikeCalculationHistoryStorage {

    private static String BIKE_HISTORY_SETTINGS = "bike_calculation_history";
    private final SharedPreferences mSharedPref;
    private Runnable mCallback;
    private boolean mIsInitialized = false;
    private static BikeCalculationHistoryStorage instance;

    private List<BikeCalculationHistoryEntry> entries;

    private BikeCalculationHistoryStorage() {
        if(instance == null) {
            this.mSharedPref = App.getContext().getSharedPreferences(BIKE_HISTORY_SETTINGS, Context.MODE_PRIVATE);
            load();
            instance = this;
        } else {
            throw new InstantiationError("No further instances allowed. Use getInstance instead.");
        }
    }

    public static BikeCalculationHistoryStorage getInstance() {
        if(instance == null) {
            instance = new BikeCalculationHistoryStorage();
        }
        return instance;
    }

    public void setCallback(Runnable callback) {
        this.mCallback = callback;
    }

    private void load() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadData();
                if(mCallback != null) {
                    mCallback.run();
                }
            }
        }).run();
    }

    private void loadData() {
        String jsonString = this.mSharedPref.getString(BIKE_HISTORY_SETTINGS, null);
        if(jsonString != null) {
            entries = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        entries.add(BikeCalculationHistoryEntry.fromJson(jsonArray.getJSONObject(i)));
                    } catch(JSONException exc1) {
                        Log.e(this.getClass().getName(), "Could not load entry no. "+i+" from settings: "+exc1.getMessage());
                        Log.d(this.getClass().getName(), "Settings: "+jsonString);
                    } catch(ParseException pexc) {
                        Log.e(this.getClass().getName(), "Could not parse date for entry no. "+i+": "+pexc.getMessage());
                        Log.d(this.getClass().getName(), "Settings: "+jsonString);
                    }
                }

            } catch(JSONException exc) {
                Log.e(this.getClass().getName(), "Could not load settings: "+exc.getMessage());
                Log.d(this.getClass().getName(), "Settings: "+jsonString);
            }

        } else {
            Log.i(this.getClass().getName(), "No settings found to load.");
        }
        mIsInitialized = true;
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
            try {
                array.put(entry.getJson());
            } catch (JSONException e) {
                Log.e(getClass().getName(), "Could not save history data entry captured at "+entry.getCaptured().toString()+": "+e.getMessage());
            }
        }
        editor.putString(BIKE_HISTORY_SETTINGS, array.toString());
        editor.commit();
    }

    public boolean isInitialized() {
        return mIsInitialized;
    }
}
