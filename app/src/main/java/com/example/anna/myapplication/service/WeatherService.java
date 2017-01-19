package com.example.anna.myapplication.service;


import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WeatherService {
    private WeatherServiceCallback callback;
    private String location;
    private Exception error;

    public WeatherService (WeatherServiceCallback callback){
        this.callback = callback;
    }

    public String getLocation() {
        return location;
    }

    public void refreshWeather (final double latitude, final double longitude) {
        new AsyncTask<Double, Void, String>() {
            @Override
            protected String doInBackground(Double... params) {
                String coordinates = "lat="+ latitude + "&lon=" + longitude;
                String endpoint = "http://api.openweathermap.org/data/2.5/weather?" + coordinates + "&appid=c4f9176a1f5a8ce8d1179ca212680667";

                try {
                    URL url = new URL(endpoint);

                    URLConnection connection = url.openConnection();

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder result = new StringBuilder();
                    String line;

                    while((line = reader.readLine()) != null){
                        result.append(line);
                    }

                    return result.toString();
                } catch (Exception e) {
                    error = e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {

                if(s == null && error != null){
                    callback.serviceFailure(error);
                    return;
                }

                try {
                    JSONObject data = new JSONObject(s);

                    callback.serviceSuccess(data);
                } catch (JSONException e) {
                    callback.serviceFailure(e);
                }
            }
        }.execute(latitude, longitude);

    }
}
