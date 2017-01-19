package com.example.anna.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anna.myapplication.data.Coord;
import com.example.anna.myapplication.data.MainWeather;
import com.example.anna.myapplication.data.Sys;
import com.example.anna.myapplication.data.Weather;
import com.example.anna.myapplication.data.Wind;
import com.example.anna.myapplication.service.WeatherService;
import com.example.anna.myapplication.service.WeatherServiceCallback;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements WeatherServiceCallback {
    static final int numberOfFavourites = 20;
    private double latitude;
    private double longitude;

    private TextView longitudeT;
    private TextView latitudeT;
    private TextView countryT;
    private TextView descriptionT;
    private TextView temperatureT;
    private TextView pressureT;
    private TextView windSpeedT;
    private TextView nameLocationT;
    private TextView sunriseT;
    private TextView sunsetT;

    private ImageView icon;

    private Button toMenuB;
    private Button changeLocationB;

    private WeatherService service;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        longitudeT = (TextView) findViewById(R.id.longitudeT);
        latitudeT = (TextView) findViewById(R.id.latitudeT);
        countryT = (TextView) findViewById(R.id.countryT);
        descriptionT = (TextView) findViewById(R.id.descriptionT);
        temperatureT = (TextView) findViewById(R.id.temperatureT);
        pressureT = (TextView) findViewById(R.id.pressureT);
        windSpeedT = (TextView) findViewById(R.id.windSpeedT);
        nameLocationT = (TextView) findViewById(R.id.nameLocationT);
        sunriseT = (TextView) findViewById(R.id.sunriseT);
        sunsetT = (TextView) findViewById(R.id.sunsetT);

        icon = (ImageView) findViewById(R.id.icon);

        toMenuB = (Button) findViewById(R.id.toMenuB);
        changeLocationB = (Button) findViewById(R.id.changePositionB);

        latitude = getIntent().getDoubleExtra("lat", 0);
        longitude = getIntent().getDoubleExtra("lon", 0);

        service = new WeatherService(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        service.refreshWeather(latitude, longitude);

        toMenuB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);

            }
        });

        changeLocationB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void serviceSuccess(JSONObject data) {
        dialog.hide();

        Coord coord = new Coord();
        coord.extract(data.optJSONObject("coord"));
        longitudeT.setText(coord.getLongitude());
        latitudeT.setText(coord.getLatitude());

        Weather weather = new Weather();
        try {
            weather.extract(data.optJSONArray("weather").getJSONObject(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        descriptionT.setText(weather.getDescription());
        int resourceId = getResources().getIdentifier("drawable/a" + weather.getIcon(), null, getPackageName());
        @SuppressWarnings("deprecation") Drawable weatherIconDrawable = getResources().getDrawable(resourceId);
        icon.setImageDrawable(weatherIconDrawable);

        MainWeather main = new MainWeather();
        main.extract(data.optJSONObject("main"));
        temperatureT.setText(String.format("%.2f",main.getTemperature()));
        pressureT.setText(String.valueOf(main.getPressure()));

        Wind wind = new Wind();
        wind.extract(data.optJSONObject("wind"));
        windSpeedT.setText(String.valueOf(wind.getSpeed()));

        Sys sys = new Sys();
        sys.extract(data.optJSONObject("sys"));
        countryT.setText(sys.getCountry());
        sunriseT.setText(String.valueOf(sys.getHourSunrise()) + ":" + String.valueOf(sys.getMinuteSunrise()) + ":" + String.valueOf(sys.getSecondSunrise()));
        sunsetT.setText(String.valueOf(sys.getHourSunset()) + ":" + String.valueOf(sys.getMinuteSunset()) + ":" + String.valueOf(sys.getSecondSunset()));

        nameLocationT.setText(data.optString("name"));
    }

    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
