package com.medspaceit.appointments.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeSlot implements Formatter{

    @SerializedName("timeslot")
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
