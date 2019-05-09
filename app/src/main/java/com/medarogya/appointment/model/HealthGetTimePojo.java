package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhupi on 06-Feb-19.
 */

public class HealthGetTimePojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("camp_time_det")
    @Expose
    public List<HealthTime> campTimeDet = null;

    public List<HealthTime> getHealthTime() {
        return campTimeDet;
    }

    public void setHealthTime(List<HealthTime> campTimeDet) {
        this.campTimeDet = campTimeDet;
    }
}
