package com.amsbcc.app;

public class StudentModel {
    public int studID;
    public String studName;
    public String studYear;
    public String studCourse;
    public String studSection;
    public String studContNum;

    public StudentModel(int studID, String studName, String studYear, String studCourse, String studSection, String studContNum) {
        this.studID = studID;
        this.studName = studName;
        this.studYear = studYear;
        this.studCourse = studCourse;
        this.studSection = studSection;
        this.studContNum = studContNum;
    }

    public StudentModel() {
    }

    @Override
    public String toString() {
        return "StudentModel{" +
                "studID=" + studID +
                ", studName='" + studName + '\'' +
                ", studYear='" + studYear + '\'' +
                ", studCourse='" + studCourse + '\'' +
                ", studSection='" + studSection + '\'' +
                ", studContNum='" + studContNum + '\'' +
                '}';
    }
}
