package com.example.anna.myapplication.service;

import org.json.JSONObject;

public interface WeatherServiceCallback {
    void serviceSuccess (JSONObject s);
    void serviceFailure (Exception exception);
}
