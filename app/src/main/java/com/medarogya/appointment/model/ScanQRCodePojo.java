package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 20-Mar-19.
 */

public class ScanQRCodePojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("details")
    @Expose
    public Details details;
    @SerializedName("total_amt")
    @Expose
    public String totalAmt;
    @SerializedName("dis_amt")
    @Expose
    public String disAmt;
    @SerializedName("message")
    @Expose
    public String message;
    public class Details {

        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("a_id")
        @Expose
        public String a_id;
        @SerializedName("city")
        @Expose
        public String city;
        @SerializedName("discount_per")
        @Expose
        public String discountPer;

    }

}
