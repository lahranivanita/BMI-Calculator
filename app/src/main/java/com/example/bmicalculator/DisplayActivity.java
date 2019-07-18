package com.example.bmicalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.renderscript.Double2;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import static android.R.attr.id;
import static com.example.bmicalculator.R.drawable.capture;


public class DisplayActivity extends AppCompatActivity {

    TextView tvBmi,tvUw,tvN,tvOw,tvO;
    Button btnSave,btnShare,btnBack;
    SharedPreferences sp;
    ImageView iv;
    TextToSpeech tts;
    FloatingActionButton fabSpeak;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        setTitle("BMI Result");
        int o= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setRequestedOrientation(o);
        tvBmi=(TextView)findViewById(R.id.tvBmi);
        tvUw=(TextView)findViewById(R.id.tvUw);
        tvN=(TextView)findViewById(R.id.tvN);
        tvOw=(TextView)findViewById(R.id.tvOw);
        tvO=(TextView)findViewById(R.id.tvO);
        btnBack=(Button)findViewById(R.id.btnBack);
        btnSave=(Button)findViewById(R.id.btnSave);
        btnShare=(Button)findViewById(R.id.btnShare);
        fabSpeak=(FloatingActionButton)findViewById(R.id.fabSpeak);
        iv=(ImageView)findViewById(R.id.iv);
        sp=getSharedPreferences("p1",MODE_PRIVATE);
        String n =sp.getString("n","");
        String p =sp.getString("p","");
        String a =sp.getString("a","");
        String gen =sp.getString("gen","");

        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(Locale.ENGLISH);
            }
        });

        final String pd = "Name: " + n + "\n" + "Age: " + a + "\n" + "Contact: " + p + "\n" + "Sex: " + gen + "\n" ;

        tvUw.setText("Below 18.5 is Underweight");
        tvN.setText("Between 18.5 to 25  is Normal");
        tvOw.setText("Between 25 to 30 is Overweight");
        tvO.setText("More than 30 is Obese");

        Intent i=getIntent();
        String msg=i.getStringExtra("bmi");
        double no = Double.parseDouble(msg);
        String ans=String.format("%.2f",no);
        final double res = Double.parseDouble(ans);
        final double wtkg = i.getDoubleExtra("wtkg",0.0);


        if(res<=18.5){
            tvBmi.setText(" Your BMI is " + res + " and you are Underweight");
            tvUw.setTextColor(Color.RED);
            }
        else if(res>18.5 & res<=25){
            tvBmi.setText("Your BMI is " + res + " and you are Normal");
            tvN.setTextColor(Color.RED);
            }
        else if(res>25 & res<=30){
            tvBmi.setText("Your BMI is " + res + " and you are Overweight");
            tvOw.setTextColor(Color.RED);
           }
        else{
            tvBmi.setText("Your BMI is " + res + " and you are Obese");
            tvO.setTextColor(Color.RED);
            }

        fabSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(res<=18.5){
                    tts.speak(" Your Body Mass Index is " + res + " and you are Underweight",TextToSpeech.QUEUE_FLUSH,null);

                }
                else if(res>18.5 & res<=25){
                    tts.speak(" Your Bodt Mass Index is " + res + " and you are Normal",TextToSpeech.QUEUE_FLUSH,null);

                }
                else if(res>25 & res<=30){
                    tts.speak(" Your Body Mass Index is " + res + " and you are Overweight",TextToSpeech.QUEUE_FLUSH,null);

                }
                else{
                    tts.speak(" Your Body Mass Index is " + res + " and you are Obese",TextToSpeech.QUEUE_FLUSH,null);

                }
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(DisplayActivity.this,CalculateActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String d= "" + new Date();
                String show="";
                if(res<18.5){
                    show="Underweight";}
                else if(res>18.5 & res<25){
                    show="Normal";}
                else if(res>25 & res<30){
                    show="Overweight";}
                else{
                    show="Obese";}
                MainActivity.db.add( d, wtkg ,res,show);

            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String o="";
                if(res<18.5){
                    o="Underweight";}
                else if(res>18.5 & res<25){
                    o="Normal";}
                else if(res>25 & res<30){
                    o="Overweight";}
                else{
                    o="Obese";}
                String bd = "BMI: " + res + "\n" + "You are " + o + "\n";
                String share = pd + bd;
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,share);
                startActivity(i);

            }
        });

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
