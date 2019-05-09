package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 05-Feb-19.
 */

public class HealthCampNotification {

    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("list")
    @Expose
    public java.util.List<List> list = null;
    public class List {

        @SerializedName("camp_id")
        @Expose
        public String campId;
        @SerializedName("from_time")
        @Expose
        public String fromTime;
        @SerializedName("to_time")
        @Expose
        public String toTime;
        @SerializedName("booking_date")
        @Expose
        public String bookingDate;
        @SerializedName("dept_name")
        @Expose
        public String deptName;
        @SerializedName("city_name")
        @Expose
        public String cityName;
}
}
