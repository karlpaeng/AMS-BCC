package com.amsbcc.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

public class SearchDate extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Button datePickerButton;
    Button selectedDateButton;

    boolean dateIsSet;
    String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_date);

        dateIsSet = false;

        datePickerButton = findViewById(R.id.datePickerBtn);
        selectedDateButton = findViewById(R.id.selectedDateBtn);

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        selectedDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dateIsSet){
                    Intent intent = new Intent(SearchDate.this, ResultView.class);
                    intent.putExtra("prev_act", "act_date");
                    intent.putExtra("date_value", datePickerButton.getText().toString());
                    startActivity(intent);
                }else{
                    Toast.makeText(SearchDate.this, "Select a valid date first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void showDatePickerDialog(){
        DatePickerDialog datePickDia = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickDia.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String zerostr = "";
        if(i2 < 10) zerostr = "0";
        String dateSelected = i + "-" + monthNames[i1] + "-" + zerostr + i2;
        datePickerButton.setText(dateSelected);
        dateIsSet = true;
    }
}