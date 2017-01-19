package com.catira.opencvdemo.model;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

/**
 * Created by Timo on 26.10.2016.
 */

public class BikeCalculationHistoryEntry implements JSONable {

    private BikeDimensions mCapturedBike;
    private PersonDimensions mPerson;
    private Calendar mCaptured;
    private Bitmap mBikeImage;

    public BikeCalculationHistoryEntry(BikeDimensions capturedBike, PersonDimensions person, Calendar captured, Bitmap bikeImage) {
        this.mCapturedBike = capturedBike;
        this.mPerson = person;
        this.mCaptured = captured;
        this.mBikeImage = bikeImage;
    }
    public BikeDimensions getCapturedBike() {
        return mCapturedBike;
    }

    public PersonDimensions getPerson() {
        return mPerson;
    }

    public Calendar getCaptured() {
        return mCaptured;
    }

    public Bitmap getBikeImage() {
        return mBikeImage;
    }

    @Override
    public JSONObject getJson() {
        try {
            JSONObject json = new JSONObject();
            json.put("bikeDimensions", mCapturedBike.getJson());
            json.put("personDimensions", mPerson.getJson());
            json.put("mCaptured", mCaptured.toString());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBikeImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();

            json.put("image", Base64.encodeToString(b, Base64.DEFAULT));

            return json;
        } catch (JSONException e) {
            Log.e(this.getClass().getName(), "Error converting object to json: "+e.getMessage());
        }
        return null;
    }
}
