package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 04-Jan-19.
 */

public class ViewCouponPojo {

    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("b_id")
    @Expose
    public String bId;
    @SerializedName("couponcode_name")
    @Expose
    public String couponcodeName;
    @SerializedName("message")
    @Expose
    public String message;
}
