package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 03-Dec-18.
 */

public class DoctorConsultFeePojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("consultation_fee")
    @Expose
    public String consultationFee;
    @SerializedName("message")
    @Expose
    public String message;
}
