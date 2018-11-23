package com.medspaceit.appointments.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 23-Nov-18.
 */

public class MyTimeList implements Formatter{


    @SerializedName("time")
    @Expose
    private String stime;

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    @Override
    public String getValue() {
        return getStime();
    }


}