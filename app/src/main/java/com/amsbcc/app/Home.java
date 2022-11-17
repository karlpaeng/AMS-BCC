package com.amsbcc.app;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class Home extends AppCompatActivity {
    Button viewRecords;
    Button loginScan;
    Button logoutScan;
    Button manageData;
    Button signOut;
    String smsBody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewRecords = (Button) findViewById(R.id.viewRecordBtn);
        manageData = (Button) findViewById(R.id.mngStudData);
        signOut = (Button) findViewById(R.id.signoutBtn);
        loginScan = (Button) findViewById(R.id.loginScan);
        //login qr scan
        loginScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smsBody = "asd";
                scanCode();

                //Toast.makeText(Home.this, "Toast", Toast.LENGTH_SHORT).show();
            }
        });
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
    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Press volume up button to turn on flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->{
        if(result.getContents() != null){
            if(true){//check query if id exists
                SmsManager mySmsManager = SmsManager.getDefault();
                mySmsManager.sendTextMessage(result.getContents(), null, smsBody, null, null);
                alertDia("SMS sent", "to" + result.getContents());
            }else{
                alertDia("Scan failed", ", This student was not found. Try again.");
            }

        }else{
            alertDia("Error", "Scan failed. Try again.");
        }
    });
    private void alertDia(String buildTitle, String buildMessage){
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setTitle(buildTitle);
        builder.setMessage(buildMessage);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
}