package com.medspaceit.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhupi on 01-Nov-18.
 */

public class TimeSlotlists {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("time_list")
    @Expose
    private java.util.List<TimeSlot> list = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<TimeSlot> getList() {
        return list;
    }

    public void setList(List<TimeSlot> list) {
        this.list = list;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
