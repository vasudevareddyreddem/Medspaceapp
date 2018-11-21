package com.medspaceit.appointments.apis;

import android.content.Context;

import com.google.gson.JsonObject;
import com.medspaceit.appointments.model.Appointment;
import com.medspaceit.appointments.model.CitiesList;
import com.medspaceit.appointments.model.CityList;
import com.medspaceit.appointments.model.DepartmentList;
import com.medspaceit.appointments.model.Doctorlists;
import com.medspaceit.appointments.model.HospitalList;
import com.medspaceit.appointments.model.Login;
import com.medspaceit.appointments.model.LoginResult;
import com.medspaceit.appointments.model.Profile;
import com.medspaceit.appointments.model.PswChange;
import com.medspaceit.appointments.model.RegResult;
import com.medspaceit.appointments.model.Registration;
import com.medspaceit.appointments.model.Specialists;
import com.medspaceit.appointments.model.StatusData;
import com.medspaceit.appointments.model.TimeSlotlists;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MyServiceDG {

    class Factory {
        public static MyServiceDG create(Context contextOfApplication) {

            // default time out is 15 seconds
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiUrl.DIAGONOSTIC_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            return retrofit.create(MyServiceDG.class);
        }
    }

    @POST(ApiUrl.cities)
    Call<CitiesList> getCities(@Body JsonObject s, @Header("content-type") String contenttype);
   }
