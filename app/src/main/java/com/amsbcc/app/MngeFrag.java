package com.amsbcc.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MngeFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MngeFrag extends Fragment {
    View v;

    Button updateNew, exportNew, clearNew;
    String tag = "";
    DBHelper dbHalp;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MngeFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MngeFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static MngeFrag newInstance(String param1, String param2) {
        MngeFrag fragment = new MngeFrag();
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
        v = inflater.inflate(R.layout.fragment_mnge, container, false);
        updateNew = v.findViewById(R.id.btnUpdateNew);
        exportNew = v.findViewById(R.id.btnExportNew);
        clearNew = v.findViewById(R.id.btnClearNew);

        updateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity) getActivity(), SelectFile.class);
                startActivity(intent);
            }
        });
        exportNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag = "export";
                alertDia("Export all Scan records?","This will create a file containing ALL Scan records currently saved in this device. The XLSX file will be saved to your local storage at Android/data/com.amsbcc.app/files/");

            }
        });
        clearNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag = "clear";
                alertDia("Clear all Scan records?","This will delete all Scan records stored in this device. Make sure to export the records first to avoid data loss.");

            }
        });

        return v;
    }


    private void alertDia(String buildTitle, String buildMessage){
        dbHalp = new DBHelper(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(buildTitle);
        builder.setMessage(buildMessage);
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbHalp = new DBHelper(getContext());
                if (tag.equals("clear")){
                    dbHalp.clearScanTable();
                    dbHalp.closeDB();
                    Toast.makeText(getContext(), "Scan records cleared", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }else if (tag.equals("export")){
                    //do export here
                    ArrayList<ScanModel> scanList = dbHalp.allScanRecords();
                    XSSFWorkbook xwb = new XSSFWorkbook();
                    XSSFSheet xsheet = xwb.createSheet("AMS BCC Data");
                    xsheet.setColumnWidth(1, 20 * 256);
                    xsheet.setColumnWidth(2, 15 * 256);
                    xsheet.setColumnWidth(3, 15 * 256);
                    int listSize = scanList.size();
                    for (int q = 0 ; q < listSize ; q++){
                        XSSFRow xRow = xsheet.createRow(q);
                        XSSFCell xCell = xRow.createCell(0);
                        xCell.setCellValue(scanList.get(q).studentID + "");
                        xCell = xRow.createCell(1);
                        xCell.setCellValue(scanList.get(q).name);
                        xCell = xRow.createCell(2);
                        xCell.setCellValue(scanList.get(q).date);
                        xCell = xRow.createCell(3);
                        xCell.setCellValue(scanList.get(q).time);
                        xCell = xRow.createCell(4);
                        xCell.setCellValue(scanList.get(q).log);
                    }
//                    XSSFRow xRow = xsheet.createRow(0);
//                    XSSFCell xCell = xRow.createCell(0);
//                    xCell.setCellValue("asd");

                    String dateNow = new SimpleDateFormat("yyyyMMMdd-hhmmssa", Locale.getDefault()).format(new Date());

                    File filePath = new File(getContext().getExternalFilesDir(null) + "/amsbcc-" + dateNow + "-exported.xlsx");;
                    try {
                        if(filePath.exists()) filePath.createNewFile();
                        else filePath = new File(getContext().getExternalFilesDir(null) + "/amsbcc-" + dateNow + "-exported.xlsx");
                        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                        xwb.write(fileOutputStream);
                        if (fileOutputStream!=null){
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                        Toast.makeText(getContext(), "File was created:" + filePath.toString(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "File failed to create", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        Log.d("asd:", e.toString());
                    }
                }else{
                    dialogInterface.dismiss();
                }
            }
        }).show();
    }
}