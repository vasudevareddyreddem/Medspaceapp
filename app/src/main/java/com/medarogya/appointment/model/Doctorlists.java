package com.medarogya.appointment.model;

/**
 * Created by Bhupi on 25-Oct-18.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Doctorlists {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("list")
    @Expose
    private java.util.List<Doctorlist> doctorlist = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public java.util.List<Doctorlist> getDoctorlist() {
        return doctorlist;
    }

    public void setDoctorlist(java.util.List<Doctorlist> doctorlist) {
        this.doctorlist = doctorlist;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
