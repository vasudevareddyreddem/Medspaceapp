package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobListUploadResumePojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("u_a_p_id")
    @Expose
    public Integer uAPId;
    @SerializedName("message")
    @Expose
    public String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getuAPId() {
        return uAPId;
    }

    public void setuAPId(Integer uAPId) {
        this.uAPId = uAPId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
