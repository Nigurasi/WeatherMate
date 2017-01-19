package com.example.anna.myapplication.data;

import org.json.JSONObject;

public class Wind implements JSONParser {
    private double speed;

    public double getSpeed() {
        return speed;
    }

    @Override
    public void extract(JSONObject data) {
        speed = data.optDouble("speed");

    }
}
