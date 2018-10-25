
package com.medspaceit.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Specialist implements Formatter
{

    @SerializedName("specialist_name")
    @Expose
    private String specialistName;

    @SerializedName("specialist_id")
    @Expose
    private String specialist_id;

    public String getSpecialistName() {
        return specialistName;
    }

    public void setSpecialistName(String specialistName) {
        this.specialistName = specialistName;
    }

    public String getSpecialist_id() {
        return specialist_id;
    }

    public void setSpecialist_id(String specialist_id) {
        this.specialist_id = specialist_id;
    }

    @Override
    public String getValue() {
        return getSpecialistName();
    }
}
