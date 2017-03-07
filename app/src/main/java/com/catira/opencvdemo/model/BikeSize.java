package com.catira.opencvdemo.model;

/**
 * Created by timos on 02.03.2017.
 */

public class BikeSize {

    private final double mFrameHeight;
    private final double mFrameLength;
    private final double mSteeringLength;
    private final double mSaddleHeight;
    private final double mCrankLength;
    public BikeSize(double frameHeight, double frameLength, double steeringLength, double saddleHeight, double crankLength) {

        this.mFrameHeight = frameHeight;
        this.mFrameLength = frameLength;
        this.mSteeringLength = steeringLength;
        this.mSaddleHeight = saddleHeight;
        this.mCrankLength = crankLength;
    }

    public double getFrameHeight() {
        return mFrameHeight;
    }

    public double getFrameLength() {
        return mFrameLength;
    }

    public double getSteeringLength() {
        return mSteeringLength;
    }

    public double getSaddleHeight() {
        return mSaddleHeight;
    }

    public double getCrankLength() {
        return mCrankLength;
    }

}
