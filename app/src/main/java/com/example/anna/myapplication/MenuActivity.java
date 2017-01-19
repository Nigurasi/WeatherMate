package com.example.anna.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MenuActivity extends AppCompatActivity {
    Button checkWeatherB;
    Button previousPositionsB;
    TextView lastAccessTV;

    private Date date = new Date();

    private static final String PREFERENCE_NAME = "myPreferences";
    private static final String LAST_ACCESS = "lastAccessDate";
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        checkWeatherB = (Button) findViewById(R.id.checkWeatherB);
        previousPositionsB = (Button) findViewById(R.id.previousPositionsB);
        lastAccessTV = (TextView) findViewById(R.id.lastAccessTV);

        preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);

        loadData();
        saveData();

        checkWeatherB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MapActivity.class);
                startActivity(intent);

            }
        });

        previousPositionsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, HistoryActivity.class);
                startActivity(intent);

            }
        });

    }

    private void loadData() {
        String textFromPreferences = preferences.getString(LAST_ACCESS, " ");

        if (textFromPreferences.equals(" ")){
            lastAccessTV.setText(date.toString());
        }
        else {
            lastAccessTV.setText(textFromPreferences);
        }
    }

    private void saveData() {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String dateText = dateFormat.format(date).toString();
        preferencesEditor.putString(LAST_ACCESS, dateText);
        preferencesEditor.commit();

        //SharedPreferences.Editor prefPos = getSharedPreferences("FAVOURITE_POSITION", MODE_PRIVATE).edit();
        //prefPos.clear().commit();


    }

}
