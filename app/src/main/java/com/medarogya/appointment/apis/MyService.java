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

public interface MyService {

    class Factory {
        public static MyService create(Context contextOfApplication) {

            // default time out is 15 seconds
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiUrl.BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            return retrofit.create(MyService.class);
        }
    }

    @POST(ApiUrl.userReg)
    Call<RegResult> registration(@Body Registration registration, @Header("content-type") String contenttype);
      @POST(ApiUrl.changePsw)
    Call<RegResult> pswChange(@Body PswChange change, @Header("content-type") String contenttype);
    @POST(ApiUrl.forgetPsw)
    Call<RegResult> forgetPsw(@Body JsonObject s, @Header("content-type") String contenttype);
    @POST(ApiUrl.citys)
    Call<CityList> getCitys(@Body JsonObject s, @Header("content-type") String contenttype);
     @POST(ApiUrl.cities)
    Call<CitiesList> getCities(@Body JsonObject s, @Header("content-type") String contenttype);
    @POST(ApiUrl.depts)
    Call<DepartmentList> getDepts(@Body JsonObject s, @Header("content-type") String contenttype);
    @POST(ApiUrl.splist)
    Call<Specialists> getSpecialists(@Body JsonObject s, @Header("content-type") String contenttype);
    @POST(ApiUrl.doctorlist)
    Call<Doctorlists> getDocterlists(@Body JsonObject s, @Header("content-type") String contenttype);
    @POST(ApiUrl.hoslist)
    Call<HospitalList> getHospitals(@Body JsonObject s, @Header("content-type") String contenttype);
    @POST(ApiUrl.timelist)
    Call<TimeSlotlists> getTimeSlots(@Body JsonObject s, @Header("content-type") String contenttype);

    @POST(ApiUrl.appointment)
    Call<RegResult> addAppointments(@Body Appointment appointment, @Header("content-type") String contenttype);
    @POST(ApiUrl.profileUpdate)
    Call<RegResult> updateProfile(@Body Profile profile, @Header("content-type") String contenttype);
    @POST(ApiUrl.statuslist)
    Call<StatusData> appList(@Body JsonObject uid, @Header("content-type") String contenttype);
    @POST(ApiUrl.history)
    Call<StatusData> appHistoryList(@Body JsonObject uid, @Header("content-type") String contenttype);
    @POST(ApiUrl.accept)
    Call<RegResult> accept(@Body JsonObject uid, @Header("content-type") String contenttype);
    @Multipart
    @POST(ApiUrl.imageUplode)
    Call<RegResult> uploadFile(@Part MultipartBody.Part file, @Part("a_u_id") RequestBody id);
    @Multipart
        @POST(ApiUrl.updateToken)
    Call<RegResult> updateToken(@Body JsonObject uid, @Header("content-type") String contenttype);
    @POST(ApiUrl.cardgenerator)
    Call<String> cardgenerator(@Body String body,@Header("content-type") String contenttype);


    @POST(ApiUrl.uploadprescription)
    Call<RegResult> uploadPrescription(@Part MultipartBody.Part file, @Part("a_u_id") RequestBody id,@Part("hos_id") RequestBody hos_id);

}
