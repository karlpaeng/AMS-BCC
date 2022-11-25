package com.amsbcc.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
public class RecAdapterByClass extends RecyclerView.Adapter<RecAdapterByClass.MyViewHolder>{
    private ArrayList<ScanModel> scanList;

    public RecAdapterByClass(ArrayList<ScanModel> scanList){
        this.scanList = scanList;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView nameTxt, dateTxt, timeTxt, logTxt;

        public MyViewHolder(final View view) {
            super(view);
            nameTxt = view.findViewById(R.id.nameTextItemForClass);
            dateTxt = view.findViewById(R.id.dateTextItemForClass);
            timeTxt = view.findViewById(R.id.timeTextItemForClass);
            logTxt = view.findViewById(R.id.logTextItemForClass);
        }
    }
        @NonNull
        @Override
        public RecAdapterByClass.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_class, parent, false);
            return new RecAdapterByClass.MyViewHolder(itemView);
        }

    @Override
    public void onBindViewHolder(@NonNull RecAdapterByClass.MyViewHolder holder, int position) {
        holder.nameTxt.setText(scanList.get(position).name);
        holder.dateTxt.setText(scanList.get(position).date);
        holder.timeTxt.setText(scanList.get(position).time);
        holder.logTxt.setText(scanList.get(position).log);
    }


        @Override
        public int getItemCount() {
            return scanList.size();
        }
}

