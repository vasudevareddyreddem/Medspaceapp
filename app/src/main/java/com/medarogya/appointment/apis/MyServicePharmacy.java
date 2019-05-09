package com.medarogya.appointment.apis;

import android.content.Context;

import com.medarogya.appointment.model.JobListUploadResumePojo;
import com.medarogya.appointment.model.RegResult;
import com.medarogya.appointment.model.RequestdataPojo;
import com.medarogya.appointment.model.UploadPharmacyPicPojo;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Bhupi on 12-Mar-19.
 */

public interface MyServicePharmacy {
    class Factorys {
        public static MyServicePharmacy create(Context contextOfApplication) {

            // default time out is 15 seconds
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiUrl.PHARMACYUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            return retrofit.create(MyServicePharmacy.class);
        }
    }

    @POST(ApiUrl.order_medicine)
    Call<UploadPharmacyPicPojo> ordermedicine(@Part MultipartBody.Part file, @Part("user_id") RequestBody uid, @Part("phar_id") RequestBody pid, @Part("address") RequestBody uaddress);

        @Multipart
    @POST(ApiUrl.apply_job)
    Call<JobListUploadResumePojo> uploadResume(@Part MultipartBody.Part file, @Part("user_id") RequestBody user_id,@Part("post_id") RequestBody post_id);

}
