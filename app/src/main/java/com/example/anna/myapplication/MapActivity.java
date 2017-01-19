package com.example.anna.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.anna.myapplication.MainActivity.numberOfFavourites;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;

    private SharedPreferences preferencesfavPos;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        preferencesfavPos = getSharedPreferences("FAVOURITE_POSITION", MODE_PRIVATE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geocoder = new Geocoder(this, Locale.getDefault());

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        AlertDialog.Builder rules = new AlertDialog.Builder(context);
        rules.setMessage("Search the map for your place and click on it when you find it. As simple as that.");
        rules.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        mMap = googleMap;
        synchronizeMap(mMap);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final Marker markerF = marker;
                marker.showInfoWindow();

                AlertDialog.Builder markerBuilder = new AlertDialog.Builder(context);
                markerBuilder.setMessage("Do you want to check weather for " + marker.getTitle() + "?");
                markerBuilder
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MapActivity.this, MainActivity.class);
                                intent.putExtra("lat", markerF.getPosition().latitude);
                                intent.putExtra("lon", markerF.getPosition().longitude);
                                intent.putExtra("name",markerF.getTitle());
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog nameDialog = markerBuilder.create();
                nameDialog.show();

                return true;
            }
        });

        LatLng kracow = new LatLng(50.061339, 19.9350843);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kracow, 15));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(final LatLng latLng) {
                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                android.location.Address address = addresses.get(0);
                StringBuilder sb = new StringBuilder();

                if (address != null) {
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++){
                        sb.append(address.getAddressLine(i) + "\n");
                    }
                }


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder
                        .setMessage("You chose: " + sb.toString() + "\nWhat do you want to do?")
                        .setCancelable(false)
                        .setPositiveButton("Check Weather For This.",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int id) {
                                Intent intent = new Intent(MapActivity.this, MainActivity.class);
                                intent.putExtra("lat", latLng.latitude);
                                intent.putExtra("lon", latLng.longitude);
                                startActivity(intent);
                            }
                        })
                        .setNeutralButton("Add This position to favourites.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog.Builder nameDialogBuilder = new AlertDialog.Builder(context);
                                final EditText nameET = new EditText(context);
                                nameDialogBuilder.setMessage("Enter Name of the Position: ");
                                nameDialogBuilder.setTitle("Name");

                                nameDialogBuilder.setView(nameET);

                                nameDialogBuilder
                                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String positionName = nameET.getText().toString();

                                                for (int i = 0; i < numberOfFavourites; i++) {
                                                    if (preferencesfavPos.contains("POSITION" + i)) {
                                                        String name = preferencesfavPos.getString("POSITION" + i, " ");
                                                        double latitude = Double.valueOf(preferencesfavPos.getString("LATITUDE" + i, "0"));
                                                        double longitude = Double.valueOf(preferencesfavPos.getString("LONGITUDE" + i, "0"));

                                                        if (name.equals(" ") && latitude == 0 && longitude == 0) {
                                                            SharedPreferences.Editor prefPos = preferencesfavPos.edit();
                                                            prefPos.putString("POSITION" + i, positionName);
                                                            prefPos.putString("LATITUDE" + i, String.valueOf(latLng.latitude));
                                                            prefPos.putString("LONGITUDE" + i, String.valueOf(latLng.longitude));
                                                            prefPos.commit();

                                                            Intent intent = new Intent(MapActivity.this, MainActivity.class);
                                                            intent.putExtra("lat", latLng.latitude);
                                                            intent.putExtra("lon", latLng.longitude);
                                                            intent.putExtra("name", positionName);
                                                            startActivity(intent);
                                                            break;
                                                        }
                                                    } else {
                                                        SharedPreferences.Editor prefPos = preferencesfavPos.edit();
                                                        prefPos.putString("POSITION" + i, positionName);
                                                        prefPos.putString("LATITUDE" + i, String.valueOf(latLng.latitude));
                                                        prefPos.putString("LONGITUDE" + i, String.valueOf(latLng.longitude));
                                                        prefPos.commit();

                                                        Intent intent = new Intent(MapActivity.this, MainActivity.class);
                                                        intent.putExtra("lat", latLng.latitude);
                                                        intent.putExtra("lon", latLng.longitude);
                                                        intent.putExtra("name", positionName);
                                                        startActivity(intent);
                                                        break;
                                                    }
                                                }
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog nameDialog = nameDialogBuilder.create();
                                nameDialog.show();

                            }
                        })
                        .setNegativeButton("Search for another place.",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
    }

    private void synchronizeMap(GoogleMap mMap) {

        for (int i = 0; i < numberOfFavourites; i ++){
            if (preferencesfavPos.contains("POSITION" + i)){
                String name = preferencesfavPos.getString("POSITION" + i, " ");
                double latitude = Double.valueOf(preferencesfavPos.getString("LATITUDE" + i, "0"));
                double longitude = Double.valueOf(preferencesfavPos.getString("LONGITUDE" + i, "0"));

                if (!name.equals(" ") && latitude != 0 && longitude != 0){
                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(name)).showInfoWindow();
                }
            }
        }
    }
}
