package com.amsbcc.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecAdapterByDate extends RecyclerView.Adapter<RecAdapterByDate.MyViewHolder>{
    private ArrayList<ScanDisplayModel> scanList;

    public RecAdapterByDate(ArrayList<ScanDisplayModel> scanList){
        this.scanList = scanList;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView nameTxt, timeTxt, logTxt;

        public MyViewHolder(final View view) {
            super(view);
            nameTxt = view.findViewById(R.id.dateTxtItem);

            timeTxt = view.findViewById(R.id.timeTxtItem);
            logTxt = view.findViewById(R.id.logTxtItem);
        }
    }
    @NonNull
    @Override
    public RecAdapterByDate.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new RecAdapterByDate.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecAdapterByDate.MyViewHolder holder, int position) {
        holder.nameTxt.setText(scanList.get(position).name);

        holder.timeTxt.setText(scanList.get(position).timeIn);
        holder.logTxt.setText(scanList.get(position).timeOut);
    }


    @Override
    public int getItemCount() {
        return scanList.size();
    }
}
