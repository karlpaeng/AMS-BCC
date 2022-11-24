package com.amsbcc.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchID extends AppCompatActivity {
    EditText idET;
    Button search;

    String idText = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_id);
        idET = findViewById(R.id.studID);
        search = findViewById(R.id.searchStudBtn);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idText = idET.getText().toString();
                if (idText.equals("")){
                    Toast.makeText(SearchID.this, "Pls type an ID", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(SearchID.this, ResultView.class);
                    intent.putExtra("prev_act", "act_id");
                    intent.putExtra("id_value", idText);
                    startActivity(intent);
                }

            }
        });
    }
}