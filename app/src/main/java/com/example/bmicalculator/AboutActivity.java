package com.example.bmicalculator;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    TextView tvAbout;
    FloatingActionButton fabCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About us");
        int o= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setRequestedOrientation(o);

        tvAbout=(TextView)findViewById(R.id.tvAbout);
        fabCall=(FloatingActionButton)findViewById(R.id.fabCall);

        tvAbout.setText(" Body mass index (BMI) is a statistical measure of your weight " +
                "scaled according to your height. Itis widely used by medical, " +
                "health and fitness professionals to classify underweight, overweight and obesity ." +
                " However, you can calculate their BMI without the use of expensive equipment or special knowledge.\n" +
                "This is an application to calculate BMI," +
                "an important factor of health in one's life.\n\nThis application is developed by " +
                "VANITA LAHRANI.\nEmail id : vanitalahrani@gmail.com");

        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:"+"7350402576"));
                startActivity(i);

            }
        });



    }
}
