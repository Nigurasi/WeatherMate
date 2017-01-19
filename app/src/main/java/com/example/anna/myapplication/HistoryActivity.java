package com.example.anna.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import static com.example.anna.myapplication.MainActivity.numberOfFavourites;

public class HistoryActivity extends AppCompatActivity {

    private Button toMenuB;
    private ListView list;
    private ArrayAdapter<String> adapter;
    Context context = this;
    private SharedPreferences preferencesfavPos;

    private Dictionary<String, Position> favPos = new Hashtable<String, Position>();
    private ArrayList<String> favPosNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        preferencesfavPos = getSharedPreferences("FAVOURITE_POSITION", MODE_PRIVATE);

        toMenuB = (Button) findViewById(R.id.toMenuB);
        list = (ListView) findViewById(R.id.previousPositionsLV);

        for (int i = 0; i < numberOfFavourites; i ++) {
            if (preferencesfavPos.contains("POSITION" + i)) {
                String name = preferencesfavPos.getString("POSITION" + i, " ");
                double latitude = Double.valueOf(preferencesfavPos.getString("LATITUDE" + i, "0"));
                double longitude = Double.valueOf(preferencesfavPos.getString("LONGITUDE" + i, "0"));

                if (!name.equals(" ") && latitude != 0 && longitude != 0) {
                    Position p1 = new Position(latitude, longitude);
                    favPos.put(name, p1);
                    favPosNames.add(name);
                }
            }
        }

        adapter = new ArrayAdapter<String>(this, R.layout.row_listbox, favPosNames);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        final String itemValue = (String) list.getItemAtPosition(position);
                                        AlertDialog.Builder favouritePosBuilder = new AlertDialog.Builder(context);
                                        favouritePosBuilder.setMessage(itemValue + "\nLat: " + favPos.get(itemValue).getLatitude() + "\nLong: " + favPos.get(itemValue).getLongitude());
                                        favouritePosBuilder.setTitle("Check weather for this position?");

                                        favouritePosBuilder
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                                                        intent.putExtra("lat", favPos.get(itemValue).getLatitude());
                                                        intent.putExtra("lon", favPos.get(itemValue).getLongitude());
                                                        intent.putExtra("name", itemValue);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        ProgressDialog progressDialog = new ProgressDialog(context);
                                                        progressDialog.setMessage("Wait...");
                                                        progressDialog.show();
                                                        for (int i = 0; i < numberOfFavourites; i++){
                                                            if (itemValue == preferencesfavPos.getString("POSITION" + i, " ")){

                                                                SharedPreferences.Editor preferencesEditor = preferencesfavPos.edit();
                                                                preferencesEditor.remove("POSITION" + i);
                                                                preferencesEditor.remove("LATITUDE" + i);
                                                                preferencesEditor.remove("LATITUDE" + i);

                                                                preferencesEditor.commit();
                                                                break;
                                                            }
                                                        }
                                                        progressDialog.hide();
                                                        Intent intent = new Intent(HistoryActivity.this, HistoryActivity.class);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog nameDialog = favouritePosBuilder.create();
                                        nameDialog.show();

                                    }
                                });

        toMenuB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, MenuActivity.class);
                startActivity(intent);

            }
        });
    }
}
