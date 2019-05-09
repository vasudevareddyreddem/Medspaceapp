package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 28-Dec-18.
 */

public class GenerateCouponPojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("b_id")
    @Expose
    public String bId;
    @SerializedName("message")
    @Expose
    public String message;
}
