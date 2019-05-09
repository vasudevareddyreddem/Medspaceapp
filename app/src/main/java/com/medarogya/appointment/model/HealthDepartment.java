package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 05-Feb-19.
 */

public class HealthDepartment implements Formatter {

    @SerializedName("camp_id")
    @Expose
    public String campId;
    @SerializedName("dept_name")
    @Expose
    public String deptName;

    public String getCampId() {
        return campId;
    }

    public void setCampId(String campId) {
        this.campId = campId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override


    public String getValue() {
        return getDeptName();
    }
}
