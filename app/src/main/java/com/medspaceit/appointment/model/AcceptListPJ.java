package com.medspaceit.appointment.model;

/**
 * Created by Bhupi on 29-Oct-18.
 */

public class AcceptListPJ {
    String date;
    String time;
    String department;
    String hospitalName;
    String hos_id;
    public AcceptListPJ(String date, String time, String department, String hospitalName, String hos_id) {
    this.date=date;
    this.time=time;
    this.department=department;
    this.hospitalName=hospitalName;
    this.hos_id=hos_id;
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

    public String getHos_id() {
        return hos_id;
    }

    public void setHos_id(String hos_id) {
        this.hos_id = hos_id;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
}
