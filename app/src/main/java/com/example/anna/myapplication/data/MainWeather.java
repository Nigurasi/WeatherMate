package com.example.anna.myapplication.data;

import org.json.JSONObject;

public class MainWeather implements JSONParser {
    private double temperature;
    private double pressure;

    public double getTemperature() {
        return temperature;
    }

    public double getPressure() {
        return pressure;
    }

    @Override
    public void extract(JSONObject data) {
        temperature = data.optDouble("temp");
        temperature -= 273.15;
        pressure = data.optDouble("pressure");

    }
}
