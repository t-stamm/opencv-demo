package com.catira.opencvdemo.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Point;

/**
 * Created by timos on 08.02.2017.
 */

public class Circle implements JSONable {

    protected Point center;

    protected int radius;

    public Circle(int x, int y, int radius){
        this.center = new Point(x, y);
        this.radius = radius;
    }

    public Circle(Point center, int radius) {
        this.center = center;
        this.radius = radius;
    }
    public Point getCenter() {
        return center;
    }

    public int getRadius() {
        return radius;
    }

    public static Circle fromJson(JSONObject obj) throws JSONException {
        return new Circle(obj.getInt("x"), obj.getInt("y"), obj.getInt("radius"));
    }

    @Override
    public JSONObject getJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("x", center.x);
        json.put("y", center.y);
        json.put("radius", radius);

        return json;
    }
}
