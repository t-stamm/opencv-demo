package com.catira.opencvdemo.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Timo on 26.10.2016.
 */

public class BikeCalculationHistoryEntry implements JSONable {

    private BikePartPositions mCapturedBike;
    private PersonDimensions mPerson;
    private Date mCaptured;
    private Bitmap mBikeImage;
    private static String DATE_FORMAT = "dd-MM-yyyy z HH:mm:ss";

    public BikeCalculationHistoryEntry(BikePartPositions capturedBike, PersonDimensions person, Date captured, Bitmap bikeImage) {
        this.mCapturedBike = capturedBike;
        this.mPerson = person;
        this.mCaptured = captured;
        this.mBikeImage = bikeImage;
    }
    public BikePartPositions getCapturedBike() {
        return mCapturedBike;
    }

    public PersonDimensions getPerson() {
        return mPerson;
    }

    public Date getCaptured() {
        return mCaptured;
    }

    public Bitmap getBikeImage() {
        return mBikeImage;
    }

    @Override
    public JSONObject getJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("bikeDimensions", mCapturedBike.getJson());
        json.put("personDimensions", mPerson.getJson());
        json.put("captured", new SimpleDateFormat(DATE_FORMAT).format(this.mCaptured));

        if(mBikeImage != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBikeImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();json.put("image", Base64.encodeToString(b, Base64.DEFAULT));
        }

        return json;
    }

    public static BikeCalculationHistoryEntry fromJson(JSONObject json) throws JSONException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        Bitmap image = null;
        if(json.has("image")) {
            byte[] base64Image = Base64.decode(json.getString("image"), Base64.DEFAULT);
            image = BitmapFactory.decodeByteArray(base64Image, 0, base64Image.length);
        }
        return new BikeCalculationHistoryEntry(BikePartPositions.fromJson(json.getJSONObject("bikeDimensions")),
                PersonDimensions.fromJson(json.getJSONObject("persionDimensions")),
                sdf.parse(json.getString("captured")),
                image);
    }
}
