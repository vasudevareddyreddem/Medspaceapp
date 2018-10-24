
package com.medspaceit.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Specialist implements Formatter
{

    @SerializedName("specialist_name")
    @Expose
    private String specialistName;

    public String getSpecialistName() {
        return specialistName;
    }

    public void setSpecialistName(String specialistName) {
        this.specialistName = specialistName;
    }

    @Override
    public String getValue() {
        return getSpecialistName();
    }
}
