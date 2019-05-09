package com.medarogya.appointment.activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhupi on 07-Mar-19.
 */

public class PharmacyUploadPic {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("message")
    @Expose
    public List<Message> message = null;
    public class Message {

        @SerializedName("phar_id")
        @Expose
        public String pharId;
        @SerializedName("name")
        @Expose
        public String name;

}
}
