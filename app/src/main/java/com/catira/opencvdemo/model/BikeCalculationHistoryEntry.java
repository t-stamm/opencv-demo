package com.catira.opencvdemo.model;

import android.media.Image;

import java.util.Calendar;

/**
 * Created by Timo on 26.10.2016.
 */

public class BikeCalculationHistoryEntry {

    private BikeDimensions capturedBike;
    private PersonDimensions person;
    private Calendar captured;
    private Image bikeImage;

    public BikeCalculationHistoryEntry(BikeDimensions capturedBike, PersonDimensions person, Calendar captured, Image bikeImage) {
        this.capturedBike = capturedBike;
        this.person = person;
        this.captured = captured;
        this.bikeImage = bikeImage;
    }
    public BikeDimensions getCapturedBike() {
        return capturedBike;
    }

    public PersonDimensions getPerson() {
        return person;
    }

    public Calendar getCaptured() {
        return captured;
    }

    public Image getBikeImage() {
        return bikeImage;
    }
}
