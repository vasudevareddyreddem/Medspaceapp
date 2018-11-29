package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by Bhupi on 25-Oct-18.
 */

public class Doctorlist implements Formatter {
    @SerializedName("doctor_name")
    @Expose
    private String doctor_name;

    @SerializedName("doctor_id")
    @Expose
    private String doctor_id;

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }
    @Override
    public String getValue() {
        return getDoctor_name();
    }
}

