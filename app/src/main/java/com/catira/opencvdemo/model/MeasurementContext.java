package com.catira.opencvdemo.model;

import android.os.Bundle;

import com.catira.opencvdemo.model.BikeDimensions;
import com.catira.opencvdemo.model.PersonDimensions;

/**
 * Created by ck on 14.02.2017.
 */

public class MeasurementContext {
    public static PersonDimensions currentPersDimen = null;
    public static String currentSettingPos = null;
    public static int currentSettingWheelSize = 0;
    public static BikeDimensions currentBikeDimen = null;

    public void onCreate(Bundle savedInstanceState){
        /*
        if (currentPersDimen != null){
            //mach was
        }
        */
    }
}
