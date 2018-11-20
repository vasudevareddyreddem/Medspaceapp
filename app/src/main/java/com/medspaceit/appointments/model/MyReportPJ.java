package com.medspaceit.appointments.model;

/**
 * Created by Bhupi on 19-Nov-18.
 */

public class MyReportPJ {
    String labname; String total; String discount; String address;
    public MyReportPJ(String labname, String total, String discount, String address) {
        this.labname=labname;
        this.total=total;
        this.discount=discount;
        this.address=address;
    }



    public String getLabname() {
        return labname;
    }

    public void setLabname(String labname) {
        this.labname = labname;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
