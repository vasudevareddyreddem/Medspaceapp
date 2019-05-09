package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 05-Feb-19.
 */

public class HealthCity implements Formatter  {
    @SerializedName("hos_bas_city")
    @Expose
    public String hosBasCity;

    public String getHosBasCity() {
        return hosBasCity;
    }

    public void setHosBasCity(String hosBasCity) {
        this.hosBasCity = hosBasCity;
    }

    @Override
    public String getValue() {
        return getHosBasCity();
    }
}
