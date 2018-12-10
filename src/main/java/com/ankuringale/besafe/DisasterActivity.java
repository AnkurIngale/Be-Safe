package com.ankuringale.besafe;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DisasterActivity extends AppCompatActivity {

    boolean doublePressedOnce = false;
    String TAG = DisasterActivity.class.getSimpleName();
    JSONObject jsonObject;
    RecyclerView rs;
    EventHandler adapter;
    List<Disaster> myDataset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disaster);
        rs = findViewById(R.id.recy);
        rs.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        myDataset = new ArrayList<>();
        getData();
    }
    public  boolean isNetworkAvailable() {
        boolean isAvailable = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected())
        {
            isAvailable = true;
        }

        return isAvailable;
    }


    @Override
    public void onBackPressed() {

        if(doublePressedOnce){
            Log.d(TAG,"App Exit!");
            finish();
        }
        doublePressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doublePressedOnce=false;
            }
        }, 2000);
    }

    public void getData()
    {
        String URL = "https://api.reliefweb.int/v1/disasters?offset=0&limit=30&preset=minimal";
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(URL).build();
            Call call = client.newCall(request);
            //asynchronous thread
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }


                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            jsonObject = new JSONObject(jsonData);
                            JSONArray ar = jsonObject.getJSONArray("data");
                            myDataset.clear();
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject a = ar.getJSONObject(i);
                                String x = a.getJSONObject("fields").getString("name").trim();
                                String title = x.substring(0,x.indexOf('-')).trim();
                                String url = a.getString("href").trim();
                                String date = x.substring(x.indexOf('-')+1,x.length()).trim();
                                myDataset.add(new Disaster(title,url,date));
                            }
                            Log.v(TAG, "Checker point 1");
                        } else {

                        }
                    } catch (IOException e) {
                        Log.e(TAG, "IO Exception", e);
                    } catch (JSONException j) {
                        Log.e(TAG, "JSON Exception", j);
                    }
                }
            });

            rs.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(DisasterActivity.this);
            rs.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            adapter = new EventHandler(myDataset, DisasterActivity.this);
            rs.setAdapter(adapter);
        }
        else{
            Toast.makeText(this,"No Internet Connection, try again!" , Toast.LENGTH_LONG).show();
        }
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch(menuItem.getItemId())
        {
            case R.id.refresh:
                getData();
                break;
            case R.id.emergency:
                Intent i = new Intent(this,EmergencyActivity.class);
                startActivity(i);
        }
        return true;
    }

}

