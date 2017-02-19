package com.catira.opencvdemo.activities;

import android.app.Activity;
import android.os.Bundle;

import com.catira.opencvdemo.model.BikeDimensions;
import com.catira.opencvdemo.model.PersonDimensions;

/**
 * Created by ck on 14.02.2017.
 */

public class MeasurmentContext extends Activity {
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
