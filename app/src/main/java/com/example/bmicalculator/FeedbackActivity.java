package com.example.bmicalculator;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class FeedbackActivity extends AppCompatActivity {
    TextView tv,tvs;
    RatingBar rabRating;
    Button btnShare;
    ImageView iva;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setTitle("Feedback");

        int o= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setRequestedOrientation(o);

        tv=(TextView)findViewById(R.id.tv);
        tvs=(TextView)findViewById(R.id.tvs);
        rabRating=(RatingBar)findViewById(R.id.rabRating);
        btnShare=(Button)findViewById(R.id.btnShare);
        iva=(ImageView)findViewById(R.id.iva);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String rating = String.valueOf(rabRating.getRating());
                String msg =  "Rating : " + rating ;
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, msg);
                startActivity(i);
            }
        });
    }
}
