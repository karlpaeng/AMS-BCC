package com.amsbcc.app;

public class ScanModel {
    public int studentID;
    public String name;
    public String date;
    public String time;
    public String log;

    public ScanModel(int studentID, String name, String date, String time, String log) {
        this.studentID = studentID;
        this.name = name;
        this.date = date;
        this.time = time;
        this.log = log;
    }

    public ScanModel() {
    }

    @Override
    public String toString() {
        return "ScanModel{" +
                "studentID=" + studentID +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", log='" + log + '\'' +
                '}';
    }
}
