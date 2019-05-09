package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 28-Dec-18.
 */

public class WalletHistoryPojo {
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

        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("amount")
        @Expose
        public String amount;
        @SerializedName("coupon_code")
        @Expose
        public String couponCode;
        @SerializedName("coupon_code_amount")
        @Expose
        public String couponCodeAmount;
        @SerializedName("purpose")
        @Expose
        public String purpose;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("doctorname")
        @Expose
        public String doctorname;
        @SerializedName("consultation_fee")
        @Expose
        public String consultationFee;
        @SerializedName("hos_bas_name")
        @Expose
        public String hosBasName;
        @SerializedName("hos_bas_city")
        @Expose
        public String hosBasCity;
        @SerializedName("date")
        @Expose
        public String date;
        @SerializedName("time")
        @Expose
        public String time;

    }
}
