package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhupi on 27-Dec-18.
 */

public class OPListPojo {

    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("totalbalance")
    @Expose
    public String totalbalance;
    @SerializedName("remainingwalletamount")
    @Expose
    public String remainingwalletamount;
    @SerializedName("usedbalanceamount")
    @Expose
    public String usedbalanceamount;
    @SerializedName("list")
    @Expose
    public java.util.List<List> list = null;
    @SerializedName("message")
    @Expose
    public String message;

    public class List {

        @SerializedName("a_u_id")
        @Expose
        public String aUId;
        @SerializedName("b_id")
        @Expose
        public String bId;
        @SerializedName("hos_id")
        @Expose
        public String hosId;
        @SerializedName("city")
        @Expose
        public String city;
        @SerializedName("patinet_name")
        @Expose
        public String patinetName;
        @SerializedName("age")
        @Expose
        public String age;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("date")
        @Expose
        public String date;
        @SerializedName("time")
        @Expose
        public String time;
        @SerializedName("department")
        @Expose
        public String department;
        @SerializedName("specialist_name")
        @Expose
        public String specialistName;
        @SerializedName("doctorname")
        @Expose
        public String doctorname;
        @SerializedName("consultation_fee")
        @Expose
        public String consultationFee;
        @SerializedName("hos_bas_name")
        @Expose
        public String hosBasName;
    }
    }
