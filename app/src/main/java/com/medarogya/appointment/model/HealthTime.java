package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 06-Feb-19.
 */

public class HealthTime implements Formatter {
    @SerializedName("camp_id")
    @Expose
    public String campId;
    @SerializedName("hos_id")
    @Expose
    public String hosId;
    @SerializedName("dept_name")
    @Expose
    public String deptName;
    @SerializedName("from_time")
    @Expose
    public String fromTime;
    @SerializedName("to_time")
    @Expose
    public String toTime;

    public String getCampId() {
        return campId;
    }

    public void setCampId(String campId) {
        this.campId = campId;
    }

    public String getHosId() {
        return hosId;
    }

    public void setHosId(String hosId) {
        this.hosId = hosId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    @Override
    public String getValue() {
        return getFromTime()+" - "+getToTime();
    }
}
