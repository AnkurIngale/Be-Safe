package com.ankuringale.besafe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class EmergencyActivity extends AppCompatActivity {

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        ActivityCompat.requestPermissions(EmergencyActivity.this, new String[]{ACCESS_FINE_LOCATION}, 1);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final Button b = findViewById(R.id.emergent);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(EmergencyActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EmergencyActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    return;
                } else {
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(location==null)
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(location==null)
                    {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        return;
                    }
                    Log.v(EmergencyActivity.class.getSimpleName(), Double.toString(location.getLatitude()));
                    Log.v(EmergencyActivity.class.getSimpleName(), Double.toString(location.getLongitude()));
                    b.setClickable(false);
                    String s = "Latitude: "+location.getLatitude()+" Longitude: "+location.getLongitude();
                    Toast.makeText(EmergencyActivity.this,s,Toast.LENGTH_LONG).show();
                }

            }

        });
    }
}
