package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 20-Feb-19.
 */

public class Pham implements Formatter{

    @SerializedName("phar_id")
    @Expose
    public String pharId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("email")
    @Expose
    public String email;

    public String getPharId() {
        return pharId;
    }

    public void setPharId(String pharId) {
        this.pharId = pharId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getValue() {
        return getName();
    }}