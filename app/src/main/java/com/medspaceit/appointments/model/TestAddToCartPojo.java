package com.medspaceit.appointments.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 21-Nov-18.
 */

public class TestAddToCartPojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("cart_id")
    @Expose
    public Integer cartId;
    @SerializedName("message")
    @Expose
    public String message;
}
