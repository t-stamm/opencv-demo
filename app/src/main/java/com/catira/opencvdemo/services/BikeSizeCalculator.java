package com.catira.opencvdemo.services;

import com.catira.opencvdemo.model.BikeSize;
import com.catira.opencvdemo.model.CyclingPosition;
import com.catira.opencvdemo.model.PersonDimensions;


/**
 * Created by Timo on 26.10.2016.
 */

public class BikeSizeCalculator {


    /*public BikePartPositions getOptimalBikeDimensions(PersonDimensions biker) {
        return null;
    }*/

    public static final double FRAME_SIZE_FACTOR = 0.665;
    public static final double SADDLE_HEIGHT_FACTOR = 0.885;

    public BikeSize calculateBikeSize(PersonDimensions person, CyclingPosition cyclingPosition) {
        return new BikeSize(getFrameHeight(person.getLegLength()), getFrameLength(person.getLegLength(), person.getBodyHeight(), person.getArmLength(), cyclingPosition), getSteeringLength(person.getLegLength()), getSaddleHeight(person.getLegLength()), getCrankLength(person.getLegLength()));
    }

    public double getFrameHeight(double valInsideLeg) {
        return valInsideLeg * FRAME_SIZE_FACTOR;
    }
    //VB = Vorbaulänge
    public double getSteeringLength(double valInsideLeg) {

        double frameSize = getFrameHeight(valInsideLeg);
        int i = (int)frameSize;

        int vB = 0;
        if (i < 55) vB = 8;
        if (i > 55 && i <= 58) vB = 10;
        if (i > 58 && i <= 61) vB = 12;
        if (i > 58 && i <= 61) vB = 14;
        if (i > 61) vB = 14;

        return vB;

    }
    //OOLength = Oberrohrlänge
    public double getFrameLength(double valInsideLeg, double trunkLength, double armLength, CyclingPosition cyclingPosition) {

        double steering = getSteeringLength(valInsideLeg);
        //Rumpflänge
        //TODOs
        // Input speichern (mbody, armelength) und dann in die Methode geben für die Berechnung.
        double seatPosIndex = 0;
        switch (cyclingPosition) {
            case TOURING:
                seatPosIndex = .52;
                break;
            case SPORT:
                seatPosIndex = .54;
                break;
            case RACE:
                seatPosIndex = .56;
                break;
        }

        double ool = (trunkLength + armLength) * seatPosIndex - steering;
        /*
        String LOG_TAG = BikeSizeCalculator.class.getSimpleName();
        Log.i(LOG_TAG, "Schrittlänge: " + valInsideLeg +
                " Rumpflänge: " + TrunkLength +
                " Armlänge: " + ArmLength +
                " Sitzpositions-Index H: " + SeatPosIndex +
                " Vorbaulänge: " + mVB
        );
        */
        return ool;

    }
    //KL = Kurbellänge (crank length)
    public double getCrankLength(double valInsideLeg) {
        double crankLength = 0;
        if(valInsideLeg < 70) crankLength = 16.5;
        else if(valInsideLeg >= 70 && valInsideLeg < 73) crankLength = 16.5;
        else if(valInsideLeg >= 73 && valInsideLeg < 76) crankLength = 16.75;
        else if(valInsideLeg >= 76 && valInsideLeg < 79) crankLength = 17.0;
        else if(valInsideLeg >= 79 && valInsideLeg < 82) crankLength = 17.25;
        else if(valInsideLeg >= 82 && valInsideLeg < 86) crankLength = 17.5;
        else if(valInsideLeg >= 86 && valInsideLeg < 90) crankLength = 17.75;
        else if(valInsideLeg >= 90) crankLength = 18.0;

        return crankLength;
    }
    public double getSaddleHeight(double valInsideLeg) {

        // 90 cm steht gerade für Schrittlänge
        return valInsideLeg * SADDLE_HEIGHT_FACTOR;
    }

}
