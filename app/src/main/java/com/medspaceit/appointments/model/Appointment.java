package com.medspaceit.appointments.model;

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
        @SerializedName("hos_id")
        @Expose
        private String hospital;
        @SerializedName("department_id")
        @Expose
        private String departmentName;
        @SerializedName("specialist_id")
        @Expose
        private String specialistName;

        @SerializedName("doctor_id")
        @Expose
        private String doctor_id;

        @SerializedName("age")
        @Expose
        private String patientAge;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("time")
        @Expose
        private String time;

        @SerializedName("name")
        @Expose
        private String name;

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


    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDoctorName(String doctor_id) {
        this.doctor_id = doctor_id;
    }
}
