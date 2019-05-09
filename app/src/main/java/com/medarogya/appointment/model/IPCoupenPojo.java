package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 28-Dec-18.
 */

public class IPCoupenPojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("a_u_id")
    @Expose
    public String aUId;
    @SerializedName("message")
    @Expose
    public String message;

}
