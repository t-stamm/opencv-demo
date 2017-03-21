package com.catira.opencvdemo.model;


/**
 * Created by Timo on 26.10.2016.
 */

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Point;
import org.opencv.core.Rect;

public class BikePartPositions implements JSONable {


    protected Circle mFrontWheel; /* Vorderrad Mittelpunkt */
    protected Circle mBackWheel; /* Hinterrad Mittelpunkt */
    protected Point mSteering; /* Lenker */

    protected Point mSteeringLength; /* Lenker Laenge */
    protected Point mPaddlesLength; /* Laenge der Pedale */
    protected Point mSaddle; /* Sattel */
    protected Point mPaddles; /* Pedale */
    protected Point mFrameBack; /* Rahmen am Sattel Startpunkt */
    protected Point mFrameFront; /* Rahmen vorne am Startpunkt des Lenkers */
    protected int mReferenceWidth; /* Referenzgroesse des Bildes zu dem die Masse passen */
    protected int mReferenceHeight; /* Referenzgroesse des Bildes zu dem die Masse passen */
    protected int mWheelSize = 0;


    public BikePartPositions(Circle mFrontWheel, Circle mBackWheel, Point mFrameFront,
                             Point mFrameBack, Point mSteering, Point mSteeringLength,
                             Point mPaddles, Point mPaddlesLength, Point mSaddle, int mWheelSize, int referenceWidth, int referenceHeight) {
        this.mFrontWheel = mFrontWheel;
        this.mBackWheel = mBackWheel;
        this.mSteering = mSteering;
        this.mSteeringLength = mSteeringLength;
        this.mPaddlesLength = mPaddlesLength;
        this.mSaddle = mSaddle;
        this.mPaddles = mPaddles;
        this.mFrameBack = mFrameBack;
        this.mFrameFront = mFrameFront;
        this.mWheelSize = mWheelSize;
        this.mReferenceWidth = referenceWidth;
        this.mReferenceHeight = referenceHeight;
    }

    /*public double getWheelbase() throws NumberFormatException {
        return Math.hypot(mFrontWheel.getCenter().x, mBackWheel.getCenter().x) + Math.hypot(mBackWheel.getCenter().x, mBackWheel.getCenter().y);
    }*/


    @Override
    public JSONObject getJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("frontWheel", mFrontWheel.getJson());
        json.put("backWheel", mBackWheel.getJson());
        json.put("steering", pointToJsonString(mSteering));
        json.put("steeringLength", pointToJsonString(mSteeringLength));
        json.put("paddles", pointToJsonString(mPaddles));
        json.put("paddlesLength", pointToJsonString(mPaddlesLength));;
        json.put("frameBack", pointToJsonString(mFrameBack));
        json.put("frameFront", pointToJsonString(mFrameFront));
        json.put("saddle", pointToJsonString(mSaddle));
        json.put("wheelSize", mWheelSize);
        json.put("referenceWidth", mReferenceWidth);
        json.put("referenceHeight", mReferenceHeight);

        return json;
    }

    private String pointToJsonString(Point p) {
        return "{x: " + p.x + ", y: " + p.y + "}";
    }

    public static BikePartPositions fromJson(JSONObject json) throws JSONException {
        JSONObject referenceRect = json.getJSONObject("referenceSize");
        return new BikePartPositions(Circle.fromJson(json.getJSONObject("frontWheel")),
                Circle.fromJson(json.getJSONObject("backWheel")),
                getPointFromJson("frameFront", json),
                getPointFromJson("frameBack", json),
                getPointFromJson("steering", json),
                getPointFromJson("steeringLength", json),
                getPointFromJson("paddles", json),
                getPointFromJson("paddlesLength", json),
                getPointFromJson("saddle", json),
                json.getInt("wheelSize"),
                json.getInt("referenceWidth"),
                json.getInt("referenceHeight"));
    }

    private static Point getPointFromJson(String name, JSONObject json) throws JSONException {
        JSONObject pointObject = json.getJSONObject(name);
        return new Point(pointObject.getDouble("x"), pointObject.getDouble("y"));
    }

    public Circle getFrontWheel() {
        return mFrontWheel;
    }

    public Circle getBackWheel() {
        return mBackWheel;
    }

    public Point getSteering() {
        return mSteering;
    }

    public Point getSteeringLength() {
        return mSteeringLength;
    }

    public Point getPaddlesLength() {
        return mPaddlesLength;
    }

    public Point getSaddle() {
        return mSaddle;
    }

    public int getWheelSize() {
        return mWheelSize;
    }

    public Point getPaddles() {
        return mPaddles;
    }

    public Point getFrameBack() {
        return mFrameBack;
    }

    public Point getFrameFront() {
        return mFrameFront;
    }

    public int getReferenceWidth() {
        return mReferenceWidth;
    }
    public int getReferenceHeight() {
        return mReferenceHeight;
    }

}

