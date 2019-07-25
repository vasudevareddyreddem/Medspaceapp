package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnlinePaymentPojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("payment_id")
    @Expose
    public Integer paymentId;
    @SerializedName("message")
    @Expose
    public String message;
}
