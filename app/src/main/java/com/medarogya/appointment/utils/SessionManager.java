package com.medarogya.appointment.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.medarogya.appointment.model.Details;
import com.medarogya.appointment.model.Login;
import com.medarogya.appointment.model.LoginResult;
import com.medarogya.appointment.model.Profile;
import com.medarogya.appointment.model.RegResult;
import com.medarogya.appointment.model.Registration;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    Context context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "MedSpace";
    public static final String Token = "tokenId";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_REMEMBER_ME = "remember_me";
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    public static final String KEY_ID = "a_u_id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD="password";
    public static final String KEY_IMAGEURI = "noImage";
    public static final String KEY_NUMBER = "mobile";
    public static final String KEY_CARDNO = "Cardnumber";
    public static final String TAG = "tag";

    public static final String PROFILE_IMG_URL = "pic_path";
    public static final String PROFILE_IMG_PATH = "profile_pic";

    // Constructor
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     *  *
     */

    public void LoginSuccess() {

        editor.putBoolean(IS_LOGIN, true);
        editor.commit();

    }

    // TODO  pack_detaill_status =1 => upgrade package,pack_detaill_status = 2 => package details

    public void createSigninSession(Login login, LoginResult result, boolean rememberme) {
        if(rememberme){
            editor.putString(KEY_PASSWORD,login.getPassword());
        }
        Details details=result.getDetails();
        editor.putString(KEY_ID,details.getAUId());
        editor.putString(KEY_NUMBER,details.getMobile());
        editor.putString(KEY_EMAIL,details.getEmail());
        editor.putString(KEY_NAME,details.getName());
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN,true);
        editor.putBoolean(KEY_REMEMBER_ME,rememberme);
        editor.putString(PROFILE_IMG_PATH,details.getPicPath());
        editor.putString(PROFILE_IMG_URL,result.getPicPath());
        editor.commit();
    }
    public void createSignUpSession(Registration registration, RegResult regResult) {
        // Storing name in pref
        editor.putString(KEY_ID, String.valueOf(regResult.getAUId()));
        editor.putString(KEY_EMAIL, registration.getEmail());
        editor.putString(KEY_NAME, registration.getName());
        editor.putString(KEY_NUMBER,registration.getMobile());
        //editor.putString(KEY_CARDNO,registration.getCardno());
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }

    public String getSingleField(String key) {
        return pref.getString(key, null);
    }
public void storeToken(String token){
        editor.putString(Token,token).commit();
}
    public void createEditProfileSession(Profile profile) {
        // Storing login value as TRUE

        // Storing name in pref
        editor.putString(KEY_EMAIL, profile.getMail());
        editor.putString(KEY_NAME, profile.getName());
        editor.putString(KEY_NUMBER,profile.getMobile());
        editor.commit();
    }

    // TODO 1 = english 2 = arbic





    /**
     *  * Get stored session data
     *  *
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name

        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        return user;
    }

    /**
     *  * Clear session details
     *  *
     */
    public void logoutUser() {
        editor.remove(IS_LOGIN);
        editor.remove(KEY_NAME);
        editor.remove(KEY_ID);
        editor.remove(PROFILE_IMG_URL);
        editor.remove(KEY_NUMBER);
        editor.remove(KEY_CARDNO);
        if(pref.getBoolean(KEY_REMEMBER_ME,false))
        {
            editor.commit();
            return;
        }
        //Clearing all data from Shared Preferences

        editor.remove(KEY_PASSWORD);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_REMEMBER_ME);
        editor.commit();


    }

    public void logoutpackged() {
        //Clearing all data from Shared Preferences

        editor.remove(IS_LOGIN);
        editor.remove(KEY_NAME);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_ID);
//        editor.remove(KEY_IMAGEURI);
        editor.remove(PROFILE_IMG_URL);
        editor.remove(PROFILE_IMG_PATH);
//        editor.remove(KEY_LANGUAGE);
        editor.commit();

       /* Intent i = new Intent(context, SelectLanguage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
        ((Activity) context).finish();*/

    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void profileImageUrl(String profile_url, String path) {
        Log.e(" profile_url : ", path + profile_url);
        editor.putString(PROFILE_IMG_URL, profile_url);
        editor.putString(PROFILE_IMG_PATH, path);
        editor.commit();
    }

    public HashMap<String, String> returnProfile_url() {
        HashMap<String, String> profileurl = new HashMap<String, String>();
        profileurl.put(PROFILE_IMG_URL, pref.getString(PROFILE_IMG_URL, null));
        profileurl.put(PROFILE_IMG_PATH, pref.getString(PROFILE_IMG_PATH, null));
        return profileurl;
    }


    public boolean isRemembered() {
        return pref.getBoolean(KEY_REMEMBER_ME, false);
    }

    public void setSingleField(String key, String value) {
        editor.putString(key,value);
        editor.commit();
    }


    public Profile getProfile() {
        Profile profile=new Profile();
        profile.setaUId(getSingleField(KEY_ID));
        profile.setMail(getSingleField(KEY_EMAIL));
        profile.setMobile(getSingleField(KEY_NUMBER));
        profile.setName(getSingleField(KEY_NAME));
        return profile;
    }


}
