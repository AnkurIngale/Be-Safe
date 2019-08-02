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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;



public class EmergencyActivity extends AppCompatActivity {


    private LocationManager locationManager;
    private String msg = "" , a;

//    public class SpinnerActivity implements AdapterView.OnItemSelectedListener {
//
//
//        public void onItemSelected(AdapterView<?> parent, View view,
//                                   int pos, long id) {
//            // An item was selected. You can retrieve the selected item using
//            // parent.getItemAtPosition(pos)
//            String a = parent.getItemAtPosition(pos).toString();
//            if(a=="1") msg +="I am ";
//            else {
//                msg +="We are ";
//                msg += a;
//            }
//            msg += "stuck here at \n";
//        }
//
//        public void onNothingSelected(AdapterView<?> parent) {
//            // Another interface callback
//            msg+="I am stuck here at \n";
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);



        Spinner spinner = (Spinner) findViewById(R.id.people_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.people, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

//        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                a = parent.getItemAtPosition(pos).toString();
                Log.d("call one :", a);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                a = "";
            }
        });


        ActivityCompat.requestPermissions(EmergencyActivity.this, new String[]{ACCESS_FINE_LOCATION}, 1);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final Button b = findViewById(R.id.emergent);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(EmergencyActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EmergencyActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                    return;
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
                    String s = "Latitude: "+location.getLatitude()+"\n"+ " Longitude: "+location.getLongitude();

                    msg +="We are ";
                    msg += a;
                    msg += " people stuck here at \n";
                    msg += s;
                    msg+='\n';
                    EditText customsg = findViewById(R.id.custom);
                    msg += customsg.getText().toString();
                    Toast.makeText(EmergencyActivity.this,s,Toast.LENGTH_LONG).show();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);

                }

            }

        });
    }
}
