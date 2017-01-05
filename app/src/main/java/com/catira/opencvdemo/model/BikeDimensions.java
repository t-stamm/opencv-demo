package com.catira.opencvdemo.model;


/**
 * Created by Timo on 26.10.2016.
 */

import org.opencv.core.Point;

/**
 * Saves the bike key points for the wheels, saddle and steering.
 * Other values are calculated
 */
public class BikeDimensions {


    protected Point mFrontWheel; /* Vorderrad Mittelpunkt */
    protected Point mBackWheel; /* Hinterrad Mittelpunkt */
    protected Point mForkHead; /* Gabelkopf */
    protected Point mSaddle; /* Sattel */
    protected Point mPaddles; /* Pedale */



    // really necessary?
    // ck no idea ;-)
    // Point  steering_head_length; /* Lenkkopflänge / - höhe */
    // Point bicycleTrail; /* Nachlauf */
    // Point bendingRecess; /* Gabelbiegung Rücksprung */

    public BikeDimensions(Point mFrontWheel, Point mBackWheel, Point mForkHead, Point mSaddle, Point mPaddles) {
        this.mFrontWheel = mFrontWheel;
        this.mBackWheel = mBackWheel;
        this.mForkHead = mForkHead;
        this.mSaddle = mSaddle;
        this.mPaddles = mPaddles;
    }

    public double getWheelbase() throws NumberFormatException {
        return Math.hypot(mFrontWheel.x, mBackWheel.x) + Math.hypot(mBackWheel.x, mBackWheel.y);
    }

    public Point getFrontWheel() {
        return mFrontWheel;
    }

    public Point getBackWheel() {
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
}

