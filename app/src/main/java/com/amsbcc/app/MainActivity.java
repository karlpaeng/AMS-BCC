package com.amsbcc.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amsbcc.app.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    TextView actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);// no dark mode
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFrag());
        binding.bottomNavigationView.setSelectedItemId(R.id.home);
        actionBar = findViewById(R.id.barView);
        //-----------------------
        DBHelper dbHalp = new DBHelper(MainActivity.this);
        if (dbHalp.checkExistingSignin()){

        }else{
            dbHalp.addSigninRecord();
        }
        if (dbHalp.getSigninStatus() == 0){
            Intent intent = new Intent(MainActivity.this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        String date = new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault()).format(new Date());
        if(!(date.equals(dbHalp.getLastSign()))){
            Toast.makeText(MainActivity.this, "You have been automatically signed out", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            dbHalp.updateSigninRecord(0);
            startActivity(intent);
        }
        //-----------------------

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.scan:
                    replaceFragment(new ScanFrag());
                    actionBar.setText("Student QR Scan");
                    break;
                case R.id.view:
                    replaceFragment(new ViewFrag());
                    actionBar.setText("View Scan Records");
                    break;
                case R.id.home:
                    replaceFragment(new HomeFrag());
                    actionBar.setText("Dashboard");
                    break;
                case R.id.mnge:
                    replaceFragment(new MngeFrag());
                    actionBar.setText("Manage Student Data");
                    break;
                case R.id.sett:
                    replaceFragment(new SettFrag());
                    actionBar.setText("Settings");
                    break;
            }


            return true;
        });



    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();

    }
}