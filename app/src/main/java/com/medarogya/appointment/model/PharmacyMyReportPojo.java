package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhupi on 19-Mar-19.
 */

public class PharmacyMyReportPojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("details")
    @Expose
    public List<Detail> details = null;
    @SerializedName("path")
    @Expose
    public String path;
    @SerializedName("message")
    @Expose
    public String message;

    public class Detail {

        @SerializedName("med_img")
        @Expose
        public String medImg;
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("phar_id")
        @Expose
        public String pharId;
        @SerializedName("address")
        @Expose
        public String address;

    }
}