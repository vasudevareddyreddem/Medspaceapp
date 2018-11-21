package com.medspaceit.appointments.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhupi on 20-Nov-18.
 */

public class SearchRelatedTest {

    @SerializedName("status")
        public Integer status;

    @SerializedName("list")
    public java.util.List<List> list = null;

    @SerializedName("imgpath")
     public String imgpath;

    @SerializedName("message")
    public String message;

    public class List {

        @SerializedName("name")

        public String name;
        @SerializedName("a_id")

        public String aId;
        @SerializedName("profile_pic")

        public Object profilePic;
        @SerializedName("test_names")

        public java.util.List<TestName> testNames = null;

    }


    public class TestName {

        @SerializedName("test_name")

        public String testName;
        @SerializedName("lab_id")

        public String labId;
        @SerializedName("l_id")

        public String lId;
        @SerializedName("test_duartion")

        public String testDuartion;
        @SerializedName("test_amount")

        public String testAmount;
        @SerializedName("test_type")

        public String testType;

    }
}