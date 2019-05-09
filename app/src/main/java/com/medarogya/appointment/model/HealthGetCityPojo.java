package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhupi on 05-Feb-19.
 */

public class HealthGetCityPojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("cities")
    @Expose
    public List<HealthCity> cities = null;

    public List<HealthCity> getCities() {
        return cities;
    }

    public void setCities(List<HealthCity> cities) {
        this.cities = cities;
    }
}
