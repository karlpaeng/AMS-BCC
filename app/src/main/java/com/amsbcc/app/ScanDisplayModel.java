package com.amsbcc.app;

public class ScanDisplayModel extends ScanModel{
    String name;
    ScanDisplayModel(String name, int studentID, String date, String timeIn, String timeOut){
        super(studentID, date, timeIn, timeOut);
        this.name = name;
    }
    ScanDisplayModel(){}

    @Override
    public String toString() {
        return "ScanDisplayModel{" +
                "name='" + name + '\'' +
                ", studentID=" + studentID +
                ", date='" + date + '\'' +
                ", timeIn='" + timeIn + '\'' +
                ", timeOut='" + timeOut + '\'' +
                '}';
    }
}
