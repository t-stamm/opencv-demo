package com.catira.opencvdemo.model;

import android.content.res.Resources;
import android.util.Log;

import com.catira.opencvdemo.App;
import com.catira.opencvdemo.R;

/**
 * Created by timos on 21.02.2017.
 */

public enum CyclingPosition {
    TOURING (R.string.cycling_position_touring),
    SPORT (R.string.cycling_position_sport),
    RACE (R.string.cycling_position_race);

    private final int mRessourceId;

    CyclingPosition(int mRessourceId) {
        this.mRessourceId = mRessourceId;
    }

    public static int[] getRessouces() {
        return new int[]{TOURING.getRessourceId(), SPORT.getRessourceId(), RACE.getRessourceId()};
    }

    public int getRessourceId() {
        return mRessourceId;
    }

    public static CyclingPosition fromString(String text) {
        try {
            Resources r = App.getContext().getResources();
            if(text.equals(r.getString(TOURING.getRessourceId()))) {
                    return TOURING;
            } else if(text.equals(r.getString(SPORT.getRessourceId()))) {
                return SPORT;
            } else if(text.equals(r.getString(RACE.getRessourceId()))) {
                return RACE;
            }
        } catch(Exception e) {
            Log.e("CyclingPosition", "Could not determine position for input '"+text+"'");
        }
        return null;
    }
}
