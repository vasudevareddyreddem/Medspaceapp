package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class couponsPojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("list")
    @Expose
    public java.util.List<List> list = null;
    @SerializedName("message")
    @Expose
    public String message;
    public class List {

        @SerializedName("cp_id")
        @Expose
        public String cpId;
        @SerializedName("c_name")
        @Expose
        public String cName;
        @SerializedName("c_type")
        @Expose
        public String cType;
        @SerializedName("c_amount")
        @Expose
        public String cAmount;
        @SerializedName("e_date")
        @Expose
        public String eDate;
        @SerializedName("status")
        @Expose
        public String status;

    }
}

