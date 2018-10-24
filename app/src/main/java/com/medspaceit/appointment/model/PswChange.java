package com.medspaceit.appointment.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PswChange {

    @SerializedName("a_u_id")
    @Expose
    private String aUId;
    @SerializedName("oldpassword")
    @Expose
    private String oldpassword;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("confirmpassword")
    @Expose
    private String confirmpassword;

    public String getAUId() {
        return aUId;
    }

    public void setAUId(String aUId) {
        this.aUId = aUId;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }

}