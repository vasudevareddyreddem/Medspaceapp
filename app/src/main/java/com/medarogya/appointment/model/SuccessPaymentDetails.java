package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.medarogya.appointment.activity.PatientDetails;

import java.util.List;

/**
 * Created by Bhupi on 23-Nov-18.
 */

public class SuccessPaymentDetails {
    @SerializedName("status")

    public Integer status;
    @SerializedName("order_id")

    public String orderId;
    @SerializedName("address_details")

    public AddressDetails addressDetails;
    @SerializedName("test_details")

    public List<TestDetail> testDetails = null;
    @SerializedName("patient_details")

    public PatientDetails patientDetails;
    @SerializedName("message")

    public String message;


    public class AddressDetails {

        @SerializedName("l_t_b_id")

        public String lTBId;
        @SerializedName("a_id")

        public String aId;
        @SerializedName("mobile")

        public String mobile;
        @SerializedName("locality")

        public String locality;
        @SerializedName("pincode")

        public String pincode;
        @SerializedName("address")

        public String address;
        @SerializedName("landmark")

        public String landmark;
        @SerializedName("address_lable")

        public String addressLable;

    }
    public class PatientDetails {

        @SerializedName("l_t_a_id")

        public String lTAId;
        @SerializedName("a_id")

        public String aId;
        @SerializedName("date")

        public String date;
        @SerializedName("time")

        public String time;
        @SerializedName("name")

        public String name;
        @SerializedName("mobile")

        public String mobile;
        @SerializedName("age")

        public String age;
        @SerializedName("email")

        public String email;
        @SerializedName("gender")

        public String gender;

    }
    public class TestDetail {

        @SerializedName("order_item_id")

        public Object orderItemId;
        @SerializedName("test_id")

        public Object testId;
        @SerializedName("delivery_charge")

        public Object deliveryCharge;
        @SerializedName("total_amt")

        public Object totalAmt;
        @SerializedName("amount")

        public Object amount;
        @SerializedName("org_amount")

        public Object orgAmount;
        @SerializedName("percentage")

        public Object percentage;
        @SerializedName("payment_type")

        public Object paymentType;
        @SerializedName("test_name")

        public Object testName;
        @SerializedName("test_package_name")

        public Object testPackageName;
        @SerializedName("test_duartion")

        public Object testDuartion;

    }
}
