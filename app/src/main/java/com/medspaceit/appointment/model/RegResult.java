package com.medspaceit.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegResult {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("a_u_id")
    @Expose
    private Integer aUId;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAUId() {
        return aUId;
    }

    public void setAUId(Integer aUId) {
        this.aUId = aUId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}