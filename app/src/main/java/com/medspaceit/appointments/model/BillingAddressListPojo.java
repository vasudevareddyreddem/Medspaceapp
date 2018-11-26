package com.medspaceit.appointments.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 26-Nov-18.
 */

public class BillingAddressListPojo {
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

        @SerializedName("l_t_b_id")
        @Expose
        public String lTBId;
        @SerializedName("a_id")
        @Expose
        public String aId;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("locality")
        @Expose
        public String locality;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("pincode")
        @Expose
        public String pincode;
        @SerializedName("landmark")
        @Expose
        public String landmark;
        @SerializedName("address_lable")
        @Expose
        public String addressLable;

    }
}
