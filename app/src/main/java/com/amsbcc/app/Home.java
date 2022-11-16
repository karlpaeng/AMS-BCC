package com.amsbcc.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home extends AppCompatActivity {
    Button viewRecords;
    Button loginScan;
    Button logoutScan;
    Button manageData;
    Button signOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewRecords = (Button) findViewById(R.id.viewRecordBtn);
        manageData = (Button) findViewById(R.id.mngStudData);
        signOut = (Button) findViewById(R.id.signoutBtn);
        //login qr scan
        //logout qr scan
        viewRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, ViewRecords.class);
                startActivity(intent);
            }
        });
        //manage data
        manageData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, ManageStudentData.class);
                startActivity(intent);
            }
        });
        //signout
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Toast.makeText(Home.this, "You have been signed out", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}