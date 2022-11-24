package com.amsbcc.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SelectFile extends AppCompatActivity {
    Button selectFile;
    TextView pathTV;
    Button importFile;

    Uri studListUri;
    String filePath;

    private static final int CODE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);
        selectFile = findViewById(R.id.fileSelectBtn);
        pathTV = findViewById(R.id.pathTextView);
        importFile = findViewById(R.id.importBtn);

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                startActivityForResult(intent, CODE);
                //pathTV.setText();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //
        pathTV.setText(data.getDataString());
        //filePath = data.;
        studListUri = data.getData();
        //uriString = uri.toString();

        //Whatever you want to do .....You can do here

        super.onActivityResult(requestCode, resultCode, data);
    }
}