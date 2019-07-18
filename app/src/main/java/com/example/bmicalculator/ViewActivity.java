package com.example.bmicalculator;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewActivity extends AppCompatActivity {
    TextView tvView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        setTitle("BMI Database");

        int o= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setRequestedOrientation(o);

        tvView = (TextView) findViewById(R.id.tvView);
        String data = MainActivity.db.viewhistory();
        if(data.length() == 0)
            tvView.setText("No results to show ! ");
        else
            tvView.setText(data);


    }
}
