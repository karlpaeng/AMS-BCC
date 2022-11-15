package com.amsbcc.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView password
    Button auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);// no dark mode
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String pw = "admin@bccams";//

        password = (TextView) findViewById(R.id.password);
        auth = (Button) findViewById(R.id.authBtn);

        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Home.class);

                if(password.getText().toString().equals(pw)){
                    Toast.makeText(MainActivity.this, "Authorization Successful", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    //goto home page
                }else{
                    Toast.makeText(MainActivity.this, "Authorization Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}