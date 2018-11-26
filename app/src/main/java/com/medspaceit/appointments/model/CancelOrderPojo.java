package com.medspaceit.appointments.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 26-Nov-18.
 */

public class CancelOrderPojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("order_item_id")
    @Expose
    public String orderItemId;
    @SerializedName("message")
    @Expose
    public String message;

}
