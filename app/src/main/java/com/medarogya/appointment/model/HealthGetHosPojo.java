package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhupi on 05-Feb-19.
 */

public class HealthGetHosPojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("hos_list")
    @Expose
    public List<HealthHospital> hosList = null;

    public List<HealthHospital> getHealthHospital() {
        return hosList;
    }

    public void setHealthHospital(List<HealthHospital> hosList) {
        this.hosList = hosList;
    }
}
