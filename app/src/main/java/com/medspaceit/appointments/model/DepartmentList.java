
package com.medspaceit.appointments.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DepartmentList {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("list")
    @Expose
    private java.util.List<Department> list = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public java.util.List<Department> getDepartment() {
        return list;
    }

    public void setDepartment(java.util.List<Department> list) {
        this.list = list;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
