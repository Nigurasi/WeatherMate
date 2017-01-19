package com.example.anna.myapplication.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anna on 11.01.2017.
 */

public class Weather implements JSONParser {
    private String description;
    private String icon;

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public void extract(JSONObject data) {
        JSONObject exactData = null;
        description = data.optString("description");
        icon = data.optString("icon");

    }
}
