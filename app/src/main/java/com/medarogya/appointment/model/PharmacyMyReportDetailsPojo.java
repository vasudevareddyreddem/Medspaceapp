package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhupi on 19-Mar-19.
 */

public class PharmacyMyReportDetailsPojo {

    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("details")
    @Expose
    public List<Detail> details = null;
    @SerializedName("message")
    @Expose
    public String message;

    public class Detail {

        @SerializedName("medicine_name")
        @Expose
        public String medicineName;
        @SerializedName("unit_price")
        @Expose
        public String unitPrice;
        @SerializedName("quantity")
        @Expose
        public String quantity;
        @SerializedName("discount")
        @Expose
        public String discount;
        @SerializedName("total")
        @Expose
        public String total;
        @SerializedName("created_date")
        @Expose
        public String createdDate;

    }
}
