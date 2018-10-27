
package com.medspaceit.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hospital implements Formatter{

    @SerializedName("hos_bas_name")
    @Expose
    private String name;

    @SerializedName("hos_id")
    @Expose
    private String id;

    @SerializedName("consultationfee")
    @Expose
    private String consultationfee;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsultationfee() {
        return consultationfee;
    }

    public void setConsultationfee(String consultationfee) {
        this.consultationfee = consultationfee;
    }

    @Override
    public String getValue() {
        return getName();
    }
}
