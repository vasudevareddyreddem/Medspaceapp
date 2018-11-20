package com.medspaceit.appointments.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medspaceit.appointments.R;
import com.medspaceit.appointments.adapters.ReportInfoAdapter;
import com.medspaceit.appointments.model.DownloadReportPJ;
import com.medspaceit.appointments.model.MyReportDownloadPoojo;
import com.medspaceit.appointments.model.MyReportPJ;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyReports extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;

    RecyclerView reportInfoRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);
        ButterKnife.bind(this);
        reportInfoRecyclerView =findViewById(R.id.reportInfoRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        reportInfoRecyclerView.setLayoutManager(llm);
        back.setOnClickListener(this);
if(isConnected()) {
    getData();
    showDialog();
}else {showToast(getString(R.string.nointernet));}   }

    private void getData() {
        StringRequest stringRequest=new StringRequest("https://api.myjson.com/bins/5z6my", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson=new Gson();
                hideDialog();
                MyReportDownloadPoojo data=gson.fromJson(response, MyReportDownloadPoojo.class);

                ReportInfoAdapter rpAdapter=new ReportInfoAdapter(MyReports.this,data);
                reportInfoRecyclerView.setAdapter(rpAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;}
    }
}
