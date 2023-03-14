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
    private ArrayList<ScanDisplayModel> scanList;

    public RecAdapterDashB(ArrayList<ScanDisplayModel> scanList){
        this.scanList = scanList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView idTxt, nameTxt, dateTxt, inTxt, outTxt;

        public MyViewHolder(final View view){
            super(view);
            idTxt = view.findViewById(R.id.tvIDDashNew);
            nameTxt = view.findViewById(R.id.tvNameDashNew);
            dateTxt = view.findViewById(R.id.tvDateDashNew);
            inTxt = view.findViewById(R.id.tvInDashNew);
            outTxt = view.findViewById(R.id.tvOutDashNew);
        }
    }
    @NonNull
    @Override
    public RecAdapterDashB.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_dashb_new, parent, false);
        return new RecAdapterDashB.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecAdapterDashB.MyViewHolder holder, int position) {

        holder.idTxt.setText("" + scanList.get(position).studentID);
        holder.nameTxt.setText("" + scanList.get(position).name);
        holder.dateTxt.setText("" + scanList.get(position).date);
        holder.inTxt.setText("" + scanList.get(position).timeIn);
        holder.outTxt.setText("" + scanList.get(position).timeOut);

    }

    @Override
    public int getItemCount() {
        return scanList.size();
    }
}
