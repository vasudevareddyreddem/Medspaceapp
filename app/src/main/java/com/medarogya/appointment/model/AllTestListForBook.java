package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhupi on 21-Nov-18.
 */

public class AllTestListForBook {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("details")
    @Expose
    public Details details;
    @SerializedName("message")
    @Expose
    public String message;


public class TestName {

    @SerializedName("test_name")
    @Expose
    public String testName;
    @SerializedName("lab_id")
    @Expose
    public String labId;
    @SerializedName("l_id")
    @Expose
    public String lId;
    @SerializedName("test_duartion")
    @Expose
    public String testDuartion;
    @SerializedName("test_amount")
    @Expose
    public String testAmount;
    @SerializedName("test_type")
    @Expose
    public String testType;

}
public class Details {

    @SerializedName("a_id")
    @Expose
    public String aId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("address1")
    @Expose
    public String address1;
    @SerializedName("address2")
    @Expose
    public String address2;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("zipcode")
    @Expose
    public String zipcode;
    @SerializedName("profile_pic")
    @Expose
    public Object profilePic;

    @SerializedName("accrediations")
    @Expose
    public Object accrediations;
    @SerializedName("test_names")
    @Expose
    public List<TestName> testNames = null;

}
}
