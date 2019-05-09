package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhupi on 06-Feb-19.
 */

public class HealthGetDatePojo {

    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("camp_date_det")
    @Expose
    public List<HealthDate> campDateDet = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<HealthDate> getHealthDate() {
        return campDateDet;
    }

    public void setHealthDate(List<HealthDate> campDateDet) {
        this.campDateDet = campDateDet;
    }
}
