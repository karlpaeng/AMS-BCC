package com.amsbcc.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.MyViewHolder> {
    private ArrayList<ScanDisplayModel> scanList;

    public RecAdapter(ArrayList<ScanDisplayModel> scanList){
        this.scanList = scanList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView dateTxt, timeTxt, logTxt;

        public MyViewHolder(final View view){
            super(view);
            dateTxt = view.findViewById(R.id.dateTxtItem);
            timeTxt = view.findViewById(R.id.timeTxtItem);
            logTxt = view.findViewById(R.id.logTxtItem);
        }
    }
    @NonNull
    @Override
    public RecAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecAdapter.MyViewHolder holder, int position) {
        holder.dateTxt.setText(scanList.get(position).date);
        holder.timeTxt.setText(scanList.get(position).timeIn);
        holder.logTxt.setText(scanList.get(position).timeOut);
    }

    @Override
    public int getItemCount() {
        return scanList.size();
    }
}
