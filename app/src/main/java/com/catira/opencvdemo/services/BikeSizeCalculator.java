package com.catira.opencvdemo.services;

import com.catira.opencvdemo.model.BikeDimensions;
import com.catira.opencvdemo.model.PersonDimensions;



/**
 * Created by Timo on 26.10.2016.
 */

public class BikeSizeCalculator {


    public BikeDimensions getOptimalBikeDimensions(PersonDimensions biker) {
        return null;
    }

    public static final double FRAME_SIZE_FACTOR = 0.665;
    public static final double SADDLE_HEIGHT_FACTOR = 0.885;

    private double mFrameSize = 0;
    private double mSaddleHeight = 0;
    private double mVB = 0;
    private double mCrankLength = 0;
    private double mOOL = 0;

    private double StringToDouble(String string) {
        // 90 cm steht gerade für Schrittlänge
        String stringRep = string.replaceAll(",",".");
        double valDouble = Double.parseDouble(stringRep);
        return valDouble;
    }
    public double getFrameSize(String valInsideLeg) {

        try {
            mFrameSize =  StringToDouble(valInsideLeg) * FRAME_SIZE_FACTOR;
        } catch (Exception e) {
            System.out.println("Expetion" + e);
        }
        return mFrameSize;
    }
    //VB = Vorbaulänge
    public double getVBLength(String valInsideLeg) {

        mFrameSize = getFrameSize(valInsideLeg);
        int i = (int)mFrameSize;

        try {
            if (i < 55) mVB = 8;
            if (i > 55 && i <= 58) mVB = 10;
            if (i > 58 && i <= 61) mVB = 12;
            if (i > 58 && i <= 61) mVB = 14;
            if (i > 61) mVB = 14;
        } catch (Exception e) {
            System.out.println("Expetion" + e);
        }

        return mVB;

    }
    //OOLength = Oberrohrlänge
    public double getOOLength(String valInsideLeg, String TrunkLength, String ArmLength, String SeatPosIndex) {

        mVB = getVBLength(valInsideLeg);
        //Rumpflänge
        double mTrunkLength = StringToDouble(TrunkLength);
        double mArmLength = StringToDouble(ArmLength);
        double indexH = StringToDouble(SeatPosIndex);
        //TODOs
        // Input speichern (mbody, armelength) und dann in die Methode geben für die Berechnung.
        mOOL = (mTrunkLength+mArmLength) * indexH - mVB;
        /*
        String LOG_TAG = BikeSizeCalculator.class.getSimpleName();
        Log.i(LOG_TAG, "Schrittlänge: " + valInsideLeg +
                " Rumpflänge: " + TrunkLength +
                " Armlänge: " + ArmLength +
                " Sitzpositions-Index H: " + SeatPosIndex +
                " Vorbaulänge: " + mVB
        );
        */
        return mOOL;

    }
    //KL = Kurbellänge (crank length)
    public double getCrankLength(String valInsideLeg) {

        double i = StringToDouble(valInsideLeg);
        try {
            if(i < 70) mCrankLength = 16.5;System.out.println("InsideLeg smaller than 70cm");
            if(i >= 70 && i < 73) mCrankLength = 16.5;
            if(i >= 73 && i < 76) mCrankLength = 16.75;
            if(i >= 76 && i < 79) mCrankLength = 17.0;
            if(i >= 79 && i < 82) mCrankLength = 17.25;
            if(i >= 82 && i < 86) mCrankLength = 17.5;
            if(i >= 86 && i < 90) mCrankLength = 17.75;
            if(i >= 90) mCrankLength = 18.0;
        } catch (Exception e) {
            System.out.println("Expetion" + e);
        }

        return mCrankLength;
    }
    public double getSaddleHeight(String valInsideLeg) {

        try {
            // 90 cm steht gerade für Schrittlänge
            mSaddleHeight = StringToDouble(valInsideLeg) * SADDLE_HEIGHT_FACTOR;

        } catch (Exception e) {
            System.out.println("Expetion" + e);
        }
        return mSaddleHeight;
    }

}
