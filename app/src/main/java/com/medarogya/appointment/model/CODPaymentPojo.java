package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 23-Nov-18.
 */

public class CODPaymentPojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("order_id")
    @Expose
    public Integer orderId;
    @SerializedName("message")
    @Expose
    public String message;
}
