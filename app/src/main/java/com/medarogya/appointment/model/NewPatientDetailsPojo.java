package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewPatientDetailsPojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("details")
    @Expose
    public List<Detail> details = null;
    @SerializedName("message")
    @Expose
    public String message;

    public class Detail {

        @SerializedName("card_number")
        @Expose
        public String cardNumber;
        @SerializedName("patient_name")
        @Expose
        public String patientName;
        @SerializedName("mobile_num")
        @Expose
        public String mobileNum;
        @SerializedName("gender")
        @Expose
        public String gender;
        @SerializedName("email_id")
        @Expose
        public String emailId;
        @SerializedName("city")
        @Expose
        public String city;

    }
}
