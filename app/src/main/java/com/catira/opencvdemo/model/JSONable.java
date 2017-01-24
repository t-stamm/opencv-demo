package com.catira.opencvdemo.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by timos on 19.01.2017.
 */
public interface JSONable {

    JSONObject getJson() throws JSONException;
}
