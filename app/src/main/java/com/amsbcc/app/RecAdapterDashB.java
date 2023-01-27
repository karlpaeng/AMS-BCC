package com.amsbcc.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecAdapterDashB extends RecyclerView.Adapter<RecAdapterDashB.MyViewHolder> {
    private ArrayList<ScanModel> scanList;

    public RecAdapterDashB(ArrayList<ScanModel> scanList){
        this.scanList = scanList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView idTxt, dateTimeTxt, logTxt;

        public MyViewHolder(final View view){
            super(view);
            idTxt = view.findViewById(R.id.idDashItem);
            dateTimeTxt = view.findViewById(R.id.dateTimeDashItem);
            logTxt = view.findViewById(R.id.logDashItem);
        }
    }
    @NonNull
    @Override
    public RecAdapterDashB.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_dashb, parent, false);
        return new RecAdapterDashB.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecAdapterDashB.MyViewHolder holder, int position) {


        String inputStr = scanList.get(position).date + "-" + scanList.get(position).time;
        String inputPattern = "yyyy-MMM-dd-hh:mm:ss a";
        String outputPattern = "yyyy/MM/dd; hh:mm:ss a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date tempDate = null;
        String str = "      Date and Time";
        try {
            tempDate = inputFormat.parse(inputStr);
            str = outputFormat.format(tempDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.idTxt.setText("" + scanList.get(position).studentID);
        if(scanList.get(position).studentID == -1) holder.idTxt.setText("Student ID");
        holder.dateTimeTxt.setText(str);
        holder.logTxt.setText(scanList.get(position).log);




    }

    @Override
    public int getItemCount() {
        return scanList.size();
    }
}
