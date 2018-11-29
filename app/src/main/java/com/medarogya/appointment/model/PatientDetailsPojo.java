package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 23-Nov-18.
 */

public class PatientDetailsPojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("patient_details_id")
    @Expose
    public Integer patientDetailsId;
    @SerializedName("message")
    @Expose
    public String message;
}
