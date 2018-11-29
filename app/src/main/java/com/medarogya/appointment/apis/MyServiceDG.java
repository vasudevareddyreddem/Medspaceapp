package com.medarogya.appointment.apis;

import android.content.Context;

import com.google.gson.JsonObject;
import com.medarogya.appointment.model.Appointment;
import com.medarogya.appointment.model.CitiesList;
import com.medarogya.appointment.model.CityList;
import com.medarogya.appointment.model.DepartmentList;
import com.medarogya.appointment.model.Doctorlists;
import com.medarogya.appointment.model.HospitalList;
import com.medarogya.appointment.model.Login;
import com.medarogya.appointment.model.LoginResult;
import com.medarogya.appointment.model.Profile;
import com.medarogya.appointment.model.PswChange;
import com.medarogya.appointment.model.RegResult;
import com.medarogya.appointment.model.Registration;
import com.medarogya.appointment.model.Specialists;
import com.medarogya.appointment.model.StatusData;
import com.medarogya.appointment.model.TimePickerList;
import com.medarogya.appointment.model.TimeSlotlists;

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
    @POST(ApiUrl.time_list)
    Call<TimePickerList> getList(@Body JsonObject s, @Header("content-type") String contenttype);

}
