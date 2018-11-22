package com.medspaceit.appointments.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartPackageDetailsPojo {
        @SerializedName("status")
        @Expose
        public Integer status;
        @SerializedName("list")
        @Expose
        public java.util.List<List> list = null;
        @SerializedName("message")
        @Expose
        public String message;


public class List {

    @SerializedName("c_id")
    @Expose
    public String cId;
    @SerializedName("test_id")
    @Expose
    public String testId;
    @SerializedName("package_id")
    @Expose
    public String packageId;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("l_id")
    @Expose
    public String lId;
    @SerializedName("test_duartion")
    @Expose
    public Object testDuartion;
    @SerializedName("delivery_charge")
    @Expose
    public String deliveryCharge;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("org_amount")
    @Expose
    public String orgAmount;
    @SerializedName("discount")
    @Expose
    public String discount;
    @SerializedName("percentage")
    @Expose
    public String percentage;
    @SerializedName("test_name")
    @Expose
    public String testName;
    @SerializedName("test_package_name")
    @Expose
    public String testPackageName;
    @SerializedName("lab_name")
    @Expose
    public String labName;
    @SerializedName("package_test_list")
    @Expose
    public java.util.List<PackageTestList> packageTestList = null;

}
public class PackageTestList {

    @SerializedName("test_package_name")
    @Expose
    public String testPackageName;
    @SerializedName("test_name")
    @Expose
    public String testName;
    @SerializedName("test_type")
    @Expose
    public String testType;
    @SerializedName("test_duartion")
    @Expose
    public String testDuartion;

}}