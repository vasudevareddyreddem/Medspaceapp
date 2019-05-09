package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 28-Dec-18.
 */

public class IPListPojo {
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

        @SerializedName("c_c_l_id")
        @Expose
        public String cCLId;
        @SerializedName("couponcode_name")
        @Expose
        public String couponcodeName;
        @SerializedName("hos_bas_name")
        @Expose
        public String hosBasName;
        @SerializedName("hos_bas_city")
        @Expose
        public String hosBasCity;
        @SerializedName("created_at")
        @Expose
        public String createdAt;

    }
}
