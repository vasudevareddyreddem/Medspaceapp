
package com.medspaceit.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Specialists {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("list")
    @Expose
    private java.util.List<Specialist> specialist = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public java.util.List<Specialist> getSpecialist() {
        return specialist;
    }

    public void setSpecialist(java.util.List<Specialist> specialist) {
        this.specialist = specialist;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
