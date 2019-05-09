package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JoblistPojo {
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

        @SerializedName("df_time")
        @Expose
        public String dfTime;
        @SerializedName("j_p_id")
        @Expose
        public String jPId;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("category")
        @Expose
        public String category;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("qualifications")
        @Expose
        public String qualifications;
        @SerializedName("experience")
        @Expose
        public String experience;
        @SerializedName("district")
        @Expose
        public String district;
        @SerializedName("last_to_apply")
        @Expose
        public String lastToApply;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("postedby")
        @Expose
        public String postedby;

    }
}