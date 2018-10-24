package com.medspaceit.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Appointment {
        @SerializedName("a_u_id")
        @Expose
        private String aUId;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("department_name")
        @Expose
        private String departmentName;
        @SerializedName("specialist_name")
        @Expose
        private String specialistName;
        @SerializedName("hos_ids")
        @Expose
        private String hosIds;
        @SerializedName("patient_age")
        @Expose
        private String patientAge;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("time")
        @Expose
        private String time;

        public String getAUId() {
        return aUId;
    }

        public void setAUId(String aUId) {
        this.aUId = aUId;
    }

        public String getCity() {
        return city;
    }

        public void setCity(String city) {
        this.city = city;
    }

        public String getDepartmentName() {
        return departmentName;
    }

        public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

        public String getSpecialistName() {
        return specialistName;
    }

        public void setSpecialistName(String specialistName) {
        this.specialistName = specialistName;
    }

        public String getHosIds() {
        return hosIds;
    }

        public void setHosIds(String hosIds) {
        this.hosIds = hosIds;
    }

        public String getPatientAge() {
        return patientAge;
    }

        public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

        public String getDate() {
        return date;
    }

        public void setDate(String date) {
        this.date = date;
    }

        public String getTime() {
        return time;
    }

        public void setTime(String time) {
        this.time = time;
    }
       public void addHos(List<Hospital> hospital){
        StringBuilder builder=new StringBuilder();
        for (Hospital hosid:hospital
             ) {
            if (builder.length()==0){
                builder.append(hosid.getId());
            }else {
                builder.append(",");
                builder.append(hosid.getId());
            }

        }

    hosIds=builder.toString();
    }

}
