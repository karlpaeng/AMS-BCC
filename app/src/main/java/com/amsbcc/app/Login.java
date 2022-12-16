package com.amsbcc.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    TextView password;
    Button auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);// no dark mode
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCompat.requestPermissions(Login.this, new String[]{
                Manifest.permission.SEND_SMS,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, PackageManager.PERMISSION_GRANTED);
        DBHelper dbHalp = new DBHelper(Login.this);

        String securityPw = "q";//
        String adminPw = "a";

        password = findViewById(R.id.password);
        auth = findViewById(R.id.authBtn);

        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                if(password.getText().toString().equals(securityPw)){
                    Toast.makeText(Login.this, "Authorization Successful", Toast.LENGTH_SHORT).show();
                    dbHalp.updateSigninRecord(1);

                    startActivity(intent);
                    //goto main Activity page
                }else if(password.getText().toString().equals(adminPw)){
                    Toast.makeText(Login.this, "Authorization Successful", Toast.LENGTH_SHORT).show();
                    dbHalp.updateSigninRecord(2);

                    startActivity(intent);
                    //go main activity page, with extra access
                }else{
                    Toast.makeText(Login.this, "Authorization Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}