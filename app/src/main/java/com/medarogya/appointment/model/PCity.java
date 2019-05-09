package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 20-Feb-19.
 */

public class PCity implements Formatter{

    @SerializedName("city")
    @Expose
    public String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getValue() {
        return getCity();
    }
}