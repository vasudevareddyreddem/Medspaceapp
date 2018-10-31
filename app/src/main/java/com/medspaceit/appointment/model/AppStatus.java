
package com.medspaceit.appointment.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppStatus implements Comparable<AppStatus>{

    int view_type=15;



    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("hos_bas_name")
    @Expose
    private String hosName;

    @SerializedName("b_id")
    @Expose
    private String bId;
    @SerializedName("hos_id")
    @Expose
    private String hosId;
    @SerializedName("patinet_name")
    @Expose
    private String patinetName;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("department")
    @Expose
    private String department;

    @SerializedName("doctor_name")
    @Expose
    private String doctor_name;
    @SerializedName("specialist_name")
    @Expose
    private String specialistName;

    @SerializedName("hos_bas_add1")
    @Expose
    private String addline1;
    @SerializedName("hos_bas_add2")
    @Expose
    private String addline2;
    @SerializedName("hos_bas_city")
    @Expose
    private String addCity;
    @SerializedName("hos_bas_state")
    @Expose
    private String state;

    public String getAddline1() {
        return addline1;
    }

    public void setAddline1(String addline1) {
        this.addline1 = addline1;
    }

    public String getAddline2() {
        return addline2;
    }

    public void setAddline2(String addline2) {
        this.addline2 = addline2;
    }

    public String getAddCity() {
        return addCity;
    }

    public void setAddCity(String addCity) {
        this.addCity = addCity;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @SerializedName("hos_bas_zipcode")
    @Expose

    private String zipcode;
    @SerializedName("couponcodes")
    @Expose
    private String couponCode;
    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getBId() {
        return bId;
    }

    public void setBId(String bId) {
        this.bId = bId;
    }
    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHosName() {
        return hosName;
    }

    public void setHosName(String hosName) {
        this.hosName = hosName;
    }

    public String getHosId() {
        return hosId;
    }

    public void setHosId(String hosId) {
        this.hosId = hosId;
    }

    public String getPatinetName() {
        return patinetName;
    }

    public void setPatinetName(String patinetName) {
        this.patinetName = patinetName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSpecialistName() {
        return specialistName;
    }

    public void setSpecialistName(String specialistName) {
        this.specialistName = specialistName;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getAddress()
    {
        StringBuilder builder=
                new StringBuilder(getAddline1())
                        .append(',')
                        .append(getAddline2())
                        .append(',')
                        .append(getAddCity())
                        .append(',')
                        .append(getState())
                        .append(',')
                        .append(getZipcode());

        return builder.toString();
    }

    @Override
    public int compareTo(@NonNull AppStatus o) {
        String myFormat = "yyyy-MM-dd"; //Change as you need
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        Date d2= null,d1=null;
        try {
            d1=sdf.parse(getDate());
            d2 = sdf.parse(o.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(d1==null||d2==null)
            return 0;

        return d1.compareTo(d2);
    }
}
