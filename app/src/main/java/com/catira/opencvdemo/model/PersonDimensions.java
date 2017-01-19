package com.catira.opencvdemo.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Timo on 26.10.2016.
 */

public class PersonDimensions implements JSONable {

    protected double mArmLength;
    protected double mLegLength; //Schrittlänge
    protected double mBodyHeight;

    public PersonDimensions(double arm_length, double leg_length, double body_height) {
        this.mArmLength = arm_length;
        this.mLegLength = leg_length;
        this.mBodyHeight = body_height;
    }

    public double getArmLength() {
        return mArmLength;
    }

    public double getLegLength() {
        return mLegLength;
    }

    public double getBodyHeight() {
        return mBodyHeight;
    }

    @Override
    public JSONObject getJson() {
        try {
            JSONObject json = new JSONObject();
            json.put("armLength", mArmLength);
            json.put("legLength", mLegLength);
            json.put("bodyHeight", mBodyHeight);

            return json;
        } catch (JSONException e) {
            Log.e(this.getClass().getName(), "Error converting "+this.getClass().getName()+" to json: "+e.getMessage());
        }
        return null;
    }
}
