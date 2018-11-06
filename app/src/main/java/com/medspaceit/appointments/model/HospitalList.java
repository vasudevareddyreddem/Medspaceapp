
package com.medspaceit.appointments.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HospitalList {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("list")
    @Expose
    private java.util.List<Hospital> list = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public java.util.List<Hospital> getHospital() {
        return list;
    }

    public void setHospital(java.util.List<Hospital> list) {
        this.list = list;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
