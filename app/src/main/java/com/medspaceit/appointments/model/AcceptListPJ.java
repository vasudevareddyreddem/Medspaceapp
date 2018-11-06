package com.medspaceit.appointments.model;

/**
 * Created by Bhupi on 29-Oct-18.
 */

public class AcceptListPJ {
    String date;
    String time;
    String department;
    String hospitalName;
    String hos_id;
    String patinet_name;
    public AcceptListPJ(String date, String time, String department, String hospitalName, String hos_id, String patinet_name) {
    this.date=date;
    this.time=time;
    this.department=department;
    this.hospitalName=hospitalName;
    this.hos_id=hos_id;
    this.patinet_name=patinet_name;
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

    public String getPatinet_name() {
        return patinet_name;
    }

    public void setPatinet_name(String patinet_name) {
        this.patinet_name = patinet_name;
    }
}
