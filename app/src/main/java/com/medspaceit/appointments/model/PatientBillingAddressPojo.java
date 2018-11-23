package com.medspaceit.appointments.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 23-Nov-18.
 */

public class PatientBillingAddressPojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("billing_id")
    @Expose
    public Integer billingId;
    @SerializedName("message")
    @Expose
    public String message;
}
