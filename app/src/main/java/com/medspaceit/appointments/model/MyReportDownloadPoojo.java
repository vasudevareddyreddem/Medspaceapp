package com.medspaceit.appointments.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhupi on 19-Nov-18.
 */

public class MyReportDownloadPoojo {
    @SerializedName("Report")
    public List<Report> report = null;


    public class Report {

        @SerializedName("sno")

        public String sno;
        @SerializedName("test")
        public String test;
        @SerializedName("labname")
        public String labname;
        @SerializedName("total")
        public String total;
        @SerializedName("discount")
        public String discount;
        @SerializedName("address")
        public String address;
        @SerializedName("viewDetails")
        public List<ViewDetail> viewDetails = null;

    }


    public class ViewDetail {

        @SerializedName("downloadreport")
        public String downloadreport;

    }



}
