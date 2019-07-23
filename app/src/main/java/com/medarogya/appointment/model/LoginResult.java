
package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResult {

    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("user_id")
    @Expose
    public UserId userId;
    @SerializedName("message")
    @Expose
    public String message;
    public class UserId {

        @SerializedName("a_u_id")
        @Expose
        public String aUId;
        @SerializedName("role")
        @Expose
        public String role;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("address")
        @Expose
        public Object address;
        @SerializedName("city")
        @Expose
        public Object city;
        @SerializedName("password")
        @Expose
        public String password;
        @SerializedName("org_password")
        @Expose
        public String orgPassword;
        @SerializedName("profile_pic")
        @Expose
        public String profilePic;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("create_at")
        @Expose
        public String createAt;
        @SerializedName("updated_at")
        @Expose
        public Object updatedAt;
        @SerializedName("token")
        @Expose
        public String token;
        @SerializedName("wallet_amount")
        @Expose
        public String walletAmount;
        @SerializedName("wallet_amount_id")
        @Expose
        public String walletAmountId;
        @SerializedName("remaining_wallet_amount")
        @Expose
        public String remainingWalletAmount;
        @SerializedName("created_by")
        @Expose
        public Object createdBy;
        @SerializedName("login_otp")
        @Expose
        public String loginOtp;
        @SerializedName("opt_created_time")
        @Expose
        public String optCreatedTime;
        @SerializedName("verified")
        @Expose
        public String verified;

    }
}
