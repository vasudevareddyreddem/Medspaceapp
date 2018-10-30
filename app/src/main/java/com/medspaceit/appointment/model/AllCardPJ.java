package com.medspaceit.appointment.model;

/**
 * Created by Bhupi on 30-Oct-18.
 */

public class AllCardPJ {
    String card_number;
    String mobile_num;
    String name;
    public AllCardPJ(String card_number, String mobile_num, String name) {
        this.card_number=card_number;
        this.name=name;
        this.mobile_num=mobile_num;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getMobile_num() {
        return mobile_num;
    }

    public void setMobile_num(String mobile_num) {
        this.mobile_num = mobile_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
