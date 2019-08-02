package com.ankuringale.besafe;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FullStoryActivity extends AppCompatActivity {

    String s;
    String TAG = FullStoryActivity.class.getSimpleName();
    JSONObject jsonObject;
    TextView title,status,date,ptype,con,story;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_story);
        
        s = getIntent().getStringExtra("url");
//Return the intent that started this activity.
//If you start an Activity with some data
//intent.putExtra("someKey", someData);
//you can retrieve this data using getIntent in the new activity:        

        title = findViewById(R.id.titlee);
        status = findViewById(R.id.status);
        date = findViewById(R.id.dateOf);
        ptype = findViewById(R.id.p_type);
        con = findViewById(R.id.coun);
        story = findViewById(R.id.story);
        story.setMovementMethod(new ScrollingMovementMethod());
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
    private void alertError()
    {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        alertDialogFragment.show(getFragmentManager() , "error_dialog");
    }
    public void getData()
        {
            String URL = s;
            if(isNetworkAvailable()) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(URL).build();
                Call call = client.newCall(request);
                //asynchronous thread
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        alertError();
                    }


                    @Override
                    public void onResponse(Call call, Response response) {
                        try {
                            String jsonData = response.body().string();
                            Log.v(TAG, jsonData);
                            if (response.isSuccessful()) {
                                jsonObject = new JSONObject(jsonData);
                                JSONArray ar = jsonObject.getJSONArray("data");
                                    JSONObject a = ar.getJSONObject(0).getJSONObject("fields");
                                    String x = a.getString("name").trim();
                                    final String titl = x.substring(0,x.indexOf('-')).trim();
                                    final String dat = x.substring(x.indexOf('-')+1,x.length()).trim();
                                    final String desc = a.getString("description").trim();
                                    final String st = a.getString("status").trim();
                                    String pt = "";
                                    String c = "";
                                    JSONArray countries = a.getJSONArray("country");
                                    for(int i = 0; i < countries.length();i++){
                                        c += countries.getJSONObject(i).getString("name")+", ";
                                    }
                                    countries = a.getJSONArray("type");
                                for(int i = 0; i < countries.length();i++){
                                    pt += countries.getJSONObject(i).getString("name")+", ";
                                }
                                final String cs = c;
                                final String pts = pt;
//as the below instructions change the UI, these need to be executed on the main thread so we use runonUIthread
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        title.setText(titl);
                                        date.setText("Date: "+dat);
                                        status.setText("Status: "+st);
                                        story.setText("Story: "+desc);
                                        ptype.setText("Reason: "+pts);
                                        con.setText("Countries Affected: "+cs);
                                    }
                                });


                                Log.v(TAG, "Checker point 2");
                            } else {
                                alertError();
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "IO Exception", e);
                        } catch (JSONException j) {
                            Log.e(TAG, "JSON Exception", j);
                        }
                    }
                });
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
