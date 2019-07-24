package com.medarogya.appointment.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medarogya.appointment.R;
import com.medarogya.appointment.adapters.couponsAdapter;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.couponsPojo;

import org.json.JSONException;
import org.json.JSONObject;

public class NewCouponActivity extends BaseActivity {
ImageView back;
RecyclerView rc_coupon_apply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_coupon);
        back=findViewById(R.id.back);
        rc_coupon_apply=findViewById(R.id.rc_coupon_apply);
        rc_coupon_apply.setLayoutManager(new GridLayoutManager(this, 1));

        if(isConnected()){
            showDialog();
            coupons();

        } else {
            showToast("no_internet");
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void coupons() {



        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    ApiUrl.NewLoginBaseUrl + ApiUrl.coupons,
                    response -> {
                        hideDialog();
                        Gson gson = new Gson();
                        couponsPojo data = gson.fromJson(String.valueOf(response), couponsPojo.class);
                        if (data.status == 0) {
                            showToast(data.message);
                        } else {
                            couponsAdapter adapter= new couponsAdapter(NewCouponActivity.this, data);
                            rc_coupon_apply.setAdapter(adapter);
                        }

                    }, error -> {

                hideDialog();

            });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onClick(View view) {

    }
}
