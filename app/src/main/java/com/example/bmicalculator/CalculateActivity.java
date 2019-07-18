package com.example.bmicalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.nearby.sharing.LocalContent;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CalculateActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks {
    TextView tvData;
    SharedPreferences sp;
    TextView tvHeight,tvFeet,tvInch,tvWeight,tvCity,tvTemp;
    Spinner spnFeet,spnInch;
    EditText etWeight;
    Button btnCalculate,btnViewHistory;
    GoogleApiClient gac;
    Location loc;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        setTitle("Calculate BMI");

        int o= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setRequestedOrientation(o);

        tvData = (TextView) findViewById(R.id.tvData);
        tvHeight = (TextView) findViewById(R.id.tvHeight);
        tvFeet = (TextView) findViewById(R.id.tvFeet);
        tvInch = (TextView) findViewById(R.id.tvInch);
        tvWeight = (TextView) findViewById(R.id.tvWeight);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvTemp = (TextView) findViewById(R.id.tvTemp);
        spnFeet = (Spinner) findViewById(R.id.spnFeet);
        spnInch = (Spinner) findViewById(R.id.spnInch);
        etWeight = (EditText) findViewById(R.id.etWeight);
        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        btnViewHistory = (Button) findViewById(R.id.btnViewHistory);
        sp = getSharedPreferences("p1", MODE_PRIVATE);
        String n = sp.getString("n", "");
        tvData.setText("Welcome " + n);

        final ArrayList<String> feet = new ArrayList<>();
        feet.add("1");
        feet.add("2");
        feet.add("3");
        feet.add("4");
        feet.add("5");
        feet.add("6");
        feet.add("7");


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, feet);

        spnFeet.setAdapter(adapter1);

        final ArrayList<String> inch = new ArrayList<>();
        inch.add("0");
        inch.add("1");
        inch.add("2");
        inch.add("3");
        inch.add("4");
        inch.add("5");
        inch.add("6");
        inch.add("7");
        inch.add("8");
        inch.add("9");
        inch.add("10");
        inch.add("11");


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, inch);

        spnInch.setAdapter(adapter2);

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(LocationServices.API);
        builder.addOnConnectionFailedListener(this);
        builder.addConnectionCallbacks(this);
        gac = builder.build();

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id1 = spnFeet.getSelectedItemPosition();
                int hfeet = Integer.parseInt(feet.get(id1));
                int id2 = spnInch.getSelectedItemPosition();
                int hinch = Integer.parseInt(inch.get(id2));
                if (etWeight.length() == 0) {
                    etWeight.setError("Enter some weight!");
                    etWeight.requestFocus();
                    return;
                }
                if (etWeight.getText().toString().equals("0")) {
                    Snackbar.make(view, "Invalid Weight!!", Snackbar.LENGTH_LONG).show();
                    etWeight.requestFocus();
                    return;
                }
                double wtkg = Double.parseDouble(etWeight.getText().toString());
                double feetm = (hfeet * 0.3048);
                double inchm = (hinch * 0.0254);
                double hmetre = feetm + inchm;
                double ans = wtkg / (hmetre * hmetre);
                String bmi = String.valueOf(ans);
                Intent i = new Intent(CalculateActivity.this, DisplayActivity.class);
                i.putExtra("bmi", bmi);
                i.putExtra("wtkg", wtkg);
                startActivity(i);
                finish();
            }
        });

        btnViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CalculateActivity.this, ViewActivity.class);
                startActivity(i);

            }
        });

    }//end of onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.website)
        {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("http://"+"en.m.wikipedia.org/wiki/Body_mass_index"));
            startActivity(i);
        }
        if(item.getItemId()==R.id.about)
        {
            Intent i = new Intent(CalculateActivity.this,AboutActivity.class);
            startActivity(i);

        }
        if(item.getItemId()==R.id.feedback)
        {
            Intent i = new Intent(CalculateActivity.this,FeedbackActivity.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onResume() {
        super.onResume();
        if(gac!=null)
            gac.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(gac!=null)
            gac.disconnect();

    }

    @Override
    public void onConnected(Bundle bundle) {
        loc=LocationServices.FusedLocationApi.getLastLocation(gac);
        if(loc!=null)
        {
            double lat = loc.getLatitude();
            double lon = loc.getLongitude();

            Geocoder g = new Geocoder(this, Locale.ENGLISH);
            try {
                List<Address> la = g.getFromLocation(lat,lon,1);
                android.location.Address add = la.get(0);

                String msg= add.getLocality();
                tvCity.setText(msg);

                String c = tvCity.getText().toString();

                String url = "http://api.openweathermap.org/";
                String sp = "data/2.5/weather?units=metric";
                String qu = "&q="+ c ;
                String id = "e4c4aadb5cf75c2f2bc18a4e2ce8ce76";
                String m = url + sp + qu + "&appid=" + id;

                MyTask mt = new MyTask();
                mt.execute(m);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else{
            Toast.makeText(this, "Open area OR Enable GPS!", Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    class MyTask extends AsyncTask<String , Void , Double>
    {
        @Override
        protected Double doInBackground(String... strings) {
            double temp=0.0;
            String line="",json="";
            try
            {
                URL url = new URL(strings[0]);
                HttpURLConnection con =(HttpURLConnection)url.openConnection();
                con.connect();
                InputStreamReader isr = new InputStreamReader(con.getInputStream());
                BufferedReader br = new BufferedReader(isr);

                while((line=br.readLine())!=null)
                {
                    json=json+line+"\n";
                }
                JSONObject o = new JSONObject(json);
                JSONObject p= o.getJSONObject("main");
                temp=p.getDouble("temp");

            } catch (Exception e) {

            }
            return temp;
        }
        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
            tvTemp.setText(""+aDouble);
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to exit ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });


        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog a = builder.create();
        a.setTitle("EXIT");
        a.show();

    }
}





























