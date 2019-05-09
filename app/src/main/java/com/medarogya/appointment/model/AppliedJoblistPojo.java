package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppliedJoblistPojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("list")
    @Expose
    public java.util.List<List> list = null;
    @SerializedName("path")
    @Expose
    public String path;
    @SerializedName("message")
    @Expose
    public String message;
    public class List {

        @SerializedName("u_a_p_id")
        @Expose
        public String uAPId;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("post_id")
        @Expose
        public String postId;
        @SerializedName("resume")
        @Expose
        public String resume;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("category")
        @Expose
        public String category;
        @SerializedName("qualifications")
        @Expose
        public String qualifications;
        @SerializedName("experience")
        @Expose
        public String experience;
        @SerializedName("district")
        @Expose
        public String district;
    }
    }
