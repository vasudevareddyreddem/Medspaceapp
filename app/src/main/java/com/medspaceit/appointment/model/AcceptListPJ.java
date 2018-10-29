package com.medspaceit.appointment.model;

/**
 * Created by Bhupi on 29-Oct-18.
 */

public class AcceptListPJ {
    String date;
    String time;
    String department;
    String hospitalName;
    public AcceptListPJ(String date, String time, String department, String hospitalName) {
    this.date=date;
    this.time=time;
    this.department=department;
    this.hospitalName=hospitalName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
}
