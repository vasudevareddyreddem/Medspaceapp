package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 05-Feb-19.
 */

public class HealthHospital implements Formatter {
    @SerializedName("hos_id")
    @Expose
    public String hosId;
    @SerializedName("hos_bas_name")
    @Expose
    public String hosBasName;

    public String getHosId() {
        return hosId;
    }

    public void setHosId(String hosId) {
        this.hosId = hosId;
    }

    public String getHosBasName() {
        return hosBasName;
    }

    public void setHosBasName(String hosBasName) {
        this.hosBasName = hosBasName;
    }

    @Override
    public String getValue() {
        return getHosBasName();
    }
}
