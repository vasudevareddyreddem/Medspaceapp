package com.medspaceit.appointments.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 21-Nov-18.
 */

public class AllPackagePojo
{
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

    @SerializedName("l_t_p_id")
    @Expose
    public String lTPId;
    @SerializedName("lab_id")
    @Expose
    public String labId;
    @SerializedName("test_package_name")
    @Expose
    public String testPackageName;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("discount")
    @Expose
    public String discount;
    @SerializedName("percentage")
    @Expose
    public String percentage;
    @SerializedName("instruction")
    @Expose
    public String instruction;
    @SerializedName("delivery_charge")
    @Expose
    public Object deliveryCharge;
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
}
}
