package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 05-Feb-19.
 */

public class HealthCampDetails {

    @SerializedName("status")
    @Expose
    public Integer status;

    @SerializedName("camp_info")
    @Expose
    public CampInfo campInfo;

    public class CampInfo {
        @SerializedName("camp_id")
        @Expose
        public String campId;
        @SerializedName("city_name")
        @Expose
        public String cityName;
        @SerializedName("hos_id")
        @Expose
        public String hosId;
        @SerializedName("dept_name")
        @Expose
        public String deptName;
        @SerializedName("booking_date")
        @Expose
        public String bookingDate;
        @SerializedName("from_time")
        @Expose
        public String fromTime;
        @SerializedName("to_time")
        @Expose
        public String toTime;
        @SerializedName("created_by")
        @Expose
        public String createdBy;
        @SerializedName("created_date")
        @Expose
        public String createdDate;
        @SerializedName("updated_date")
        @Expose
        public Object updatedDate;
        @SerializedName("status")
        @Expose
        public Object status;
        @SerializedName("hos_bas_name")
        @Expose
        public String hosBasName;
    }
}