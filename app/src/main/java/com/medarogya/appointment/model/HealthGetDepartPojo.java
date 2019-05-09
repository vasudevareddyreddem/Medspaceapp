package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhupi on 05-Feb-19.
 */

public class HealthGetDepartPojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("dept_list")
    @Expose
    public List<HealthDepartment> deptList = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<HealthDepartment> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<HealthDepartment> deptList) {
        this.deptList = deptList;
    }
}
