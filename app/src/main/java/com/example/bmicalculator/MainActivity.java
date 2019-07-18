package com.example.bmicalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etName,etAge,etPhone;
    TextView tvLabel;
    Button btnRegister;
    RadioGroup rgGender;
    SharedPreferences sp;
    static MyDbHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Registeration");

        int o= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setRequestedOrientation(o);

        etName=(EditText)findViewById(R.id.etName);
        etAge=(EditText)findViewById(R.id.etAge);
        etPhone=(EditText)findViewById(R.id.etPhone);
        tvLabel=(TextView)findViewById(R.id.tvLabel);
        btnRegister=(Button)findViewById(R.id.btnRegister);
        rgGender=(RadioGroup)findViewById(R.id.rgGender);
        sp=getSharedPreferences("p1",MODE_PRIVATE);
        db = new MyDbHandler(this);

        String n =sp.getString("n","");
        if(n.length()!= 0)
        {
            Intent i = new Intent(MainActivity.this,CalculateActivity.class);
            startActivity(i);
            finish();
        }
        else {
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String x=etName.getText().toString();
                    char[] y=x.toCharArray();
                    for(char n:y)
                    {
                        if (!Character.isLetter(n))
                        {
                            Snackbar.make(view, "Invalid Name!!", Snackbar.LENGTH_LONG).show();
                            etName.setText("");
                            etName.requestFocus();
                            break;
                        }
                    }
                    if (etName.length() == 0) {
                        etName.setError("Enter Name!");
                        etName.requestFocus();
                        return;
                    } else if (etAge.length() == 0) {
                        etAge.setError("Enter some Age!");
                        etAge.requestFocus();
                        return;
                    } else if (etPhone.length() == 0) {
                        etPhone.setError("Enter Phone Number!");
                        etPhone.requestFocus();
                        return;
                    } else if (etAge.getText().toString().equals("0")) {
                        Snackbar.make(view, "Invalid Age!!", Snackbar.LENGTH_LONG).show();
                        etAge.requestFocus();
                        return;
                    } else if (etPhone.length() != 10) {
                        Snackbar.make(view, "Invalid Phone Number!!", Snackbar.LENGTH_LONG).show();
                        etPhone.requestFocus();
                        return;
                    } else {
                        Toast.makeText(MainActivity.this, "Entry Successful!", Toast.LENGTH_SHORT).show();

                        int r1=rgGender.getCheckedRadioButtonId();
                        RadioButton rb=(RadioButton)findViewById(r1);
                        String gen=rb.getText().toString();
                        String n=etName.getText().toString();
                        String p=etPhone.getText().toString();
                        String a=etAge.getText().toString();

                        SharedPreferences.Editor editor=sp.edit();
                        editor.putString("n",n);
                        editor.putString("p",p);
                        editor.putString("a",a);
                        editor.putString("gen",gen);
                        editor.commit();



                        Intent i = new Intent(MainActivity.this,CalculateActivity.class);
                        startActivity(i);
                        finish();


                    }
                }
            });
        }
    }
}
