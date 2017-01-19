package com.example.anna.myapplication.data;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class Sys implements JSONParser {
    private String country;
    private long hourSunrise, minuteSunrise, secondSunrise;
    private long hourSunset, minuteSunset, secondSunset;

    public String getCountry() {
        return country;
    }

    public long getHourSunrise() {
        return hourSunrise;
    }

    public long getMinuteSunrise() {
        return minuteSunrise;
    }

    public long getSecondSunrise() {
        return secondSunrise;
    }

    public long getHourSunset() {
        return hourSunset;
    }

    public long getMinuteSunset() {
        return minuteSunset;
    }

    public long getSecondSunset() {
        return secondSunset;
    }

    @Override
    public void extract(JSONObject data) {
        long secondsSunrise, secondsSunset;
        country = data.optString("country");
        secondsSunrise = data.optLong("sunrise");
        secondsSunset = data.optLong("sunset");

        int daySunrise = (int) TimeUnit.SECONDS.toDays(secondsSunrise);
        hourSunrise = TimeUnit.SECONDS.toHours(secondsSunrise) - (daySunrise *24);
        minuteSunrise = TimeUnit.SECONDS.toMinutes(secondsSunrise) - (TimeUnit.SECONDS.toHours(secondsSunrise)* 60);
        secondSunrise = TimeUnit.SECONDS.toSeconds(secondsSunrise) - (TimeUnit.SECONDS.toMinutes(secondsSunrise) *60);

        int daySunset = (int) TimeUnit.SECONDS.toDays(secondsSunset);
        hourSunset = TimeUnit.SECONDS.toHours(secondsSunset) - (daySunset *24);
        minuteSunset = TimeUnit.SECONDS.toMinutes(secondsSunset) - (TimeUnit.SECONDS.toHours(secondsSunset)* 60);
        secondSunset = TimeUnit.SECONDS.toSeconds(secondsSunset) - (TimeUnit.SECONDS.toMinutes(secondsSunset) *60);

    }
}
