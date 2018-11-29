
package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Department implements Formatter{

    @SerializedName("department_name")
    @Expose
    private String departmentName;


  @SerializedName("department_id")
    @Expose
    private String department_id;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    @Override
    public String getValue() {
        return getDepartmentName();
    }
}
