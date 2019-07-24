package com.medarogya.appointment.apis;

import android.content.Context;

import com.google.gson.JsonObject;
import com.medarogya.appointment.model.CitiesList;
import com.medarogya.appointment.model.Login;
import com.medarogya.appointment.model.LoginResult;
import com.medarogya.appointment.model.RegResult;
import com.medarogya.appointment.model.Registration;
import com.medarogya.appointment.model.TimePickerList;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface MyServiceNewLogin {
    class Factoryes {
        public static MyServiceNewLogin create(Context contextOfApplication) {

            // default time out is 15 seconds
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiUrl.NewLoginBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            return retrofit.create(MyServiceNewLogin.class);
        }
    }

    @POST(ApiUrl.userReg)
    Call<RegResult> registration(@Body Registration registration, @Header("content-type") String contenttype);

}
