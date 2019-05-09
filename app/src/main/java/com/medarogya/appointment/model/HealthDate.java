package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 06-Feb-19.
 */

public class HealthDate implements Formatter {
    @SerializedName("hos_id")
    @Expose
    public String hosId;
    @SerializedName("dept_name")
    @Expose
    public String deptName;
    @SerializedName("booking_date")
    @Expose
    public String bookingDate;

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

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    @Override
    public String getValue() {
        return getBookingDate();
    }
}
