package com.medspaceit.appointment.model;

public class TimeSlot implements Formatter {
    String value="";

    public TimeSlot(String value) {
        this.value = value;
    }

    public void setValue(String value){
        this.value=value;
    }
    @Override
    public String getValue() {
        return this.value;
    }
}
