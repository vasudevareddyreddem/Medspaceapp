
package com.medspaceit.appointments.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StatusData {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("list")
    @Expose
    private List<AppStatus> list = null;
    @SerializedName("a_u_id")
    @Expose
    private String aUId;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<AppStatus> getList() {
        return list;
    }

    public void setList(List<AppStatus> list) {
        this.list = list;
    }

    public String getAUId() {
        return aUId;
    }

    public void setAUId(String aUId) {
        this.aUId = aUId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
