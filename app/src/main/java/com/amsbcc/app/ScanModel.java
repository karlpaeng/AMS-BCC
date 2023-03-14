package com.amsbcc.app;

public class ScanModel {
    public int studentID;
    public String date;
    public String timeIn;
    public String timeOut;

    public ScanModel(int studentID, String date, String timeIn, String timeOut) {
        this.studentID = studentID;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    public ScanModel() {
    }

    @Override
    public String toString() {
        return "ScanModel{" +
                "studentID=" + studentID +
                ", date='" + date + '\'' +
                ", timeIn='" + timeIn + '\'' +
                ", timeOut='" + timeOut + '\'' +
                '}';
    }
}
