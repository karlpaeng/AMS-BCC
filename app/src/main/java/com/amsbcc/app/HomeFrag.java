package com.amsbcc.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFrag extends Fragment {
    View v;
    RecyclerView recViewDash;
    TextView date, in, out, current;
    Button btnBack, btnForw;
    DBHelper dbHalp;

    int pageCounter;
    private ArrayList<ScanDisplayModel> scanList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFrag newInstance(String param1, String param2) {
        HomeFrag fragment = new HomeFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);
        date = v.findViewById(R.id.dateTextDash);
        in = v.findViewById(R.id.totalSignIn);
        out = v.findViewById(R.id.totalSignOut);
        recViewDash = v.findViewById(R.id.recViewDash);

        btnBack = v.findViewById(R.id.btnBack);
        btnForw = v.findViewById(R.id.btnForw);

        current = v.findViewById(R.id.CurrentLogins);

        dbHalp = new DBHelper(getContext());

        date.setText( ((MainActivity) getActivity()).currDate);
        in.setText("" + ((MainActivity) getActivity()).inCount);
        out.setText("" + ((MainActivity) getActivity()).outCount);

        current.setText("" + (((MainActivity) getActivity()).inCount - ((MainActivity) getActivity()).outCount));
//        in.setText("0000");
//        out.setText("0000");
        pageCounter = 0;
        updateList(pageCounter);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageCounter--;
                if (pageCounter < 0){
                    pageCounter = 0;
                }else {
                    updateList(pageCounter);
                }
            }
        });
        btnForw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageCounter++;
                if ((pageCounter*6)>=dbHalp.scanTableSize()){
                    pageCounter--;
                }else {
                    updateList(pageCounter);
                }
            }
        });



        return v;
    }
    private void updateList(int page){
        scanList = dbHalp.getRecentScans(page);
        RecAdapterDashB adapter = new RecAdapterDashB(scanList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recViewDash.setLayoutManager(layoutManager);
        recViewDash.setItemAnimator(new DefaultItemAnimator());
        recViewDash.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //Toast.makeText(getContext(), "asd" + pageCounter, Toast.LENGTH_SHORT).show();
    }
}