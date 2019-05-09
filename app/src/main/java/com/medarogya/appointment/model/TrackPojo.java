package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupi on 19-Mar-19.
 */

public class TrackPojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("status_msg")
    @Expose
    public String statusMsg;
    @SerializedName("message")
    @Expose
    public String message;
}
