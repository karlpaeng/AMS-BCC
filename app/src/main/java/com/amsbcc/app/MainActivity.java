package com.amsbcc.app;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amsbcc.app.databinding.ActivityMainBinding;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    TextView actionBar;
    ArrayList<ScanDisplayModel> recentScans;

    String smsBody, logDB, logSMS;
    Calendar calendar;
    SimpleDateFormat simpleDate, simpleTime;
    String dateStr, timeStr, currDate;
    int outCount, inCount;
    DBHelper dbHalp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);// no dark mode
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setdate
        setTextData();
        replaceFragment(new HomeFrag());
        binding.bottomNavigationView.setSelectedItemId(R.id.home);
        actionBar = findViewById(R.id.barView);
        //-----------------------
        dbHalp = new DBHelper(MainActivity.this);
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
                    //setdate
                    setTextData();
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
    public void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Press volume up button to turn on flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->{

        if(result.getContents() != null){
            StudentModel student = new StudentModel();
            try {
                 student = dbHalp.searchStudentByID(Integer.parseInt(result.getContents()));
            }catch (Exception e){
                e.printStackTrace();
                alertDia(e.toString(), "QR code is not a student ID");
                student.studID = -1;
            }
            if(student.studID != -1){//check query if id exists at student table, get name,contact from id @ the db

                calendar = Calendar.getInstance();
                simpleDate = new SimpleDateFormat("yyyy-MMM-dd");
                simpleTime = new SimpleDateFormat("hh:mm:ss a");
                dateStr = simpleDate.format(calendar.getTime());
                timeStr = simpleTime.format(calendar.getTime());

                smsBody = dateStr + ":" + student.studName + " has been logged " + logDB + logSMS + " Baao Community College at " + timeStr;
                boolean doSend = false;
                if(logDB.equals("in")){
                    ScanModel scan = new ScanModel(
                            student.studID,
                            dateStr,
                            timeStr,
                            "-"
                    );
                    dbHalp.timeInScanToDB(scan);
                    alertDia("LOG" + logDB.toUpperCase() + " Scan successful", student.studID + ":" + student.studName);
                    doSend = true;
                }else if (logDB.equals("out")){
                    int tempScanId = dbHalp.getExistingTimeIn(student.studID, dateStr);
                    if(tempScanId == -1){
                        alertDia("ERROR: NO LOGIN RECORD", "This Student has not been signed in yet. Logout scan not recorded.");
                        doSend = false;
                    }else{
                        dbHalp.timeOutScanToDB(tempScanId, timeStr);
                        alertDia("LOG" + logDB.toUpperCase() + " Scan successful", student.studID + ":" + student.studName);
                        doSend = true;
                    }
                }
                //--------
                if (doSend){
                    sendSMS(student.getStudContNum());
                }

            }else{
                alertDia("Scan failed", "This student was not found. Try again.");
            }
            //alertDia("Error", "Scan failed. Try again." + result.getContents().toString());
            //Toast.makeText(MainActivity.this, "not null"+result.getContents().toString(), Toast.LENGTH_SHORT).show();

        }else{
            alertDia("Error", "Scan failed. Try again.");
            //alertDia("Error2", "2Scan failed. Try again.");
            //Toast.makeText(MainActivity.this, "null", Toast.LENGTH_SHORT).show();
        }
    });
    private void sendSMS(String contact){
        try{
            SmsManager mySmsManager = SmsManager.getDefault();
            mySmsManager.sendTextMessage(contact, null, smsBody, null, null);
            Toast.makeText(MainActivity.this, "SMS was sent", Toast.LENGTH_SHORT).show();

            //"LOG" + logDB.toUpperCase() + " Scan successful"
            //student id : name

            //temporary
        }catch (Exception e){
            e.printStackTrace();
            alertDia("SMS not Sent", "The SMS Notification failed to send.");
        }
    }
    private void alertDia(String buildTitle, String buildMessage){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(buildTitle);
        builder.setMessage(buildMessage);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
    public void signOutFunction(){
        dbHalp = new DBHelper(MainActivity.this);
        Intent intent = new Intent(MainActivity.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        dbHalp.updateSigninRecord(0);
        startActivity(intent);
    }
    public void setTextData(){
        calendar = Calendar.getInstance();
        simpleDate = new SimpleDateFormat("yyyy, MMMM dd, EEEE");
        currDate = simpleDate.format(calendar.getTime());//-----------------
        simpleDate = new SimpleDateFormat("yyyy-MMM-dd");
        dbHalp = new DBHelper(MainActivity.this);
        outCount = dbHalp.getScanCount(simpleDate.format(calendar.getTime()), "out");//-----------------
        inCount = dbHalp.getScanCount(simpleDate.format(calendar.getTime()), "in");//-----------------
        recentScans = dbHalp.getRecentScans();
    }
}