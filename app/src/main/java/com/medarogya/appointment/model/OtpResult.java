package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpResult {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("details")
    @Expose
    public Details details;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("pic_path")
    @Expose
    public String picPath;
    public class Details {

        @SerializedName("a_u_id")
        @Expose
        public String aUId;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("profile_pic")
        @Expose
        public String profilePic;
        @SerializedName("login_otp")
        @Expose
        public String loginOtp;

    }
}
