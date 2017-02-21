package com.catira.opencvdemo.model;


/**
 * Created by Timo on 26.10.2016.
 */

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Point;

/**
 * Saves the bike key points for the wheels, saddle and steering.
 * Other values are calculated
 */
public class BikeDimensions implements JSONable {


    protected Circle mFrontWheel; /* Vorderrad Mittelpunkt */
    protected Circle mBackWheel; /* Hinterrad Mittelpunkt */
    protected Point mForkHead; /* Gabelkopf */


    protected Point mSaddle; /* Sattel */
    protected Point mPaddles; /* Pedale */



    // really necessary?
    // ck no idea ;-)
    // Point  steering_head_length; /* Lenkkopflänge / - höhe */
    // Point bicycleTrail; /* Nachlauf */
    // Point bendingRecess; /* Gabelbiegung Rücksprung */

    public BikeDimensions(Circle mFrontWheel, Circle mBackWheel, Point mForkHead, Point mSaddle, Point mPaddles) {
        this.mFrontWheel = mFrontWheel;
        this.mBackWheel = mBackWheel;
        this.mForkHead = mForkHead;
        this.mSaddle = mSaddle;
        this.mPaddles = mPaddles;
    }

    public double getWheelbase() throws NumberFormatException {
        return Math.hypot(mFrontWheel.getCenter().x, mBackWheel.getCenter().x) + Math.hypot(mBackWheel.getCenter().x, mBackWheel.getCenter().y);
    }

    public Circle getFrontWheel() {
        return mFrontWheel;
    }

    public Circle getBackWheel() {
        return mBackWheel;
    }

    public Point getForkHead() {
        return mForkHead;
    }

    public Point getSaddle() {
        return mSaddle;
    }

    public Point getPaddles() {
        return mPaddles;
    }

    public void setFrontWheel(Circle mFrontWheel) {
        this.mFrontWheel = mFrontWheel;
    }

    public void setBackWheel(Circle mBackWheel) {
        this.mBackWheel = mBackWheel;
    }

    public void setForkHead(Point mForkHead) {
        this.mForkHead = mForkHead;
    }

    public void setSaddle(Point mSaddle) {
        this.mSaddle = mSaddle;
    }

    public void setPaddles(Point mPaddles) {
        this.mPaddles = mPaddles;
    }

    @Override
    public JSONObject getJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("frontWheel", mFrontWheel.getJson());
        json.put("backWheel", mBackWheel.getJson());
        json.put("forkHead", "{x: " + mForkHead.x + ", y: " + mForkHead.y + "}");
        json.put("saddle", "{x: " + mSaddle.x + ", y: " + mSaddle.y + "}");
        json.put("paddles", "{x: " + mPaddles.x + ", y: " + mPaddles.y + "}");

        return json;
    }

    public static BikeDimensions fromJson(JSONObject json) throws JSONException {
        return new BikeDimensions(Circle.fromJson(json.getJSONObject("frontWheel")),
                Circle.fromJson(json.getJSONObject("backWheel")),
                getPointFromJson("forkHead", json),
                getPointFromJson("saddle", json),
                getPointFromJson("paddles", json));
    }

    private static Point getPointFromJson(String name, JSONObject json) throws JSONException {
        JSONObject pointObject = json.getJSONObject(name);
        return new Point(pointObject.getDouble("x"), pointObject.getDouble("y"));
    }
}

