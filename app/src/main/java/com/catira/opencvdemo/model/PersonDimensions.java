package com.catira.opencvdemo.model;

/**
 * Created by Timo on 26.10.2016.
 */

public class PersonDimensions {

    protected double arm_length;
    protected double leg_length; //Schrittlänge
    protected double snout_vent_length; //Rumpflänge
    protected double body_height;

    public PersonDimensions(double arm_length, double leg_length, double body_height, double snout_vent_length) {
        this.arm_length = arm_length;
        this.leg_length = leg_length;
        this.body_height = body_height;
        this.snout_vent_length = snout_vent_length;
    }

    public double getArm_length() {
        return arm_length;
    }

    public double getLeg_length() {
        return leg_length;
    }

    public double getBody_height() {
        return body_height;
    }
}
