package com.medarogya.appointment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medarogya.appointment.R;
import com.medarogya.appointment.adapters.AllPackageInfoAdapter;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.AllPackagePojo;
import com.medarogya.appointment.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Lab extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;


    @BindView(R.id.select_labtest_ll)
    LinearLayout select_labtest_ll;
    @BindView(R.id.select_booklabtest_ll)
    LinearLayout select_booklabtest_ll;
    @BindView(R.id.select_myreport_ll)
    LinearLayout select_myreport_ll;
    @BindView(R.id.package_recyclerview)
    RecyclerView package_recyclerview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        select_labtest_ll.setOnClickListener(this);
        select_booklabtest_ll.setOnClickListener(this);
        select_myreport_ll.setOnClickListener(this);
        //package_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        package_recyclerview.setLayoutManager(new GridLayoutManager(this, 2));


        if (isConnected()) {
            showDialog();
            getPackages();

        } else {
            showToast(getString(R.string.nointernet));
        }
    }

    private void getPackages() {


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = ApiUrl.DIAGONOSTIC_BASE_URL + ApiUrl.packages;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Gson gson = new Gson();
                String UID=manager.getSingleField(SessionManager.KEY_ID);
                AllPackagePojo data = gson.fromJson(response, AllPackagePojo.class);
                if(data.status==0)
                {
                    showToast(data.message);
                }
                else {
                AllPackageInfoAdapter apAdapter = new AllPackageInfoAdapter(Lab.this, data,UID);
                package_recyclerview.setAdapter(apAdapter);
            }}
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.select_myreport_ll:
                startActivity(new Intent(this, MyReports.class));
                break;
            case R.id.select_labtest_ll:
                startActivity(new Intent(this, SelectLabTest.class));
                break;
            case R.id.select_booklabtest_ll:
                startActivity(new Intent(this, CallToBookLabTest.class));
                break;

        }
    }


}
