package com.example.anna.myapplication.data;

import org.json.JSONObject;

public class Coord implements JSONParser {
    private String longitude;
    private String latitude;

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    @Override
    public void extract(JSONObject data) {
        longitude = data.optString("lon");
        latitude = data.optString("lat");

    }
}
