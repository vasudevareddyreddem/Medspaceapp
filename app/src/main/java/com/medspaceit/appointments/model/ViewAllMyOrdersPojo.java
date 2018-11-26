package com.medspaceit.appointments.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhupi on 19-Nov-18.
 */

public class ViewAllMyOrdersPojo {

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

        @SerializedName("order_item_id")
        @Expose
        public String orderItemId;
        @SerializedName("lab_status")
        @Expose
        public String labStatus;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("payment_type")
        @Expose
        public String paymentType;
        @SerializedName("delivery_charge")
        @Expose
        public String deliveryCharge;
        @SerializedName("amount")
        @Expose
        public String amount;
        @SerializedName("test_name")
        @Expose
        public String testName;
        @SerializedName("test_package_name")
        @Expose
        public String testPackageName;
        @SerializedName("test_duartion")
        @Expose
        public String testDuartion;
        @SerializedName("p_name")
        @Expose
        public String pName;
        @SerializedName("mobile")
        @Expose
        public String mobile;

    }
}