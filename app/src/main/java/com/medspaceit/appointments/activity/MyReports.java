package com.medspaceit.appointments.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medspaceit.appointments.R;
import com.medspaceit.appointments.adapters.ReportInfoAdapter;
import com.medspaceit.appointments.model.DownloadReportPJ;
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

    List<MyReportPJ>reportInfoList;
    List<DownloadReportPJ>DownloadReportList;
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

        getData();
    }

    private void getData() {
        StringRequest stringRequest=new StringRequest("https://api.myjson.com/bins/5z6my", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    reportInfoList=new ArrayList<>();
                    DownloadReportList=new ArrayList<>();
                    Log.e("dfsdfsdf===",response);


                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray =jsonObject.getJSONArray("Report");
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        JSONObject job=jsonArray.getJSONObject(i);
                        String labname=job.getString("labname");
                        String total=job.getString("total");
                        String discount=job.getString("discount");
                        String address=job.getString("address");

                        MyReportPJ mrp=new MyReportPJ(labname,total,discount,address);
                        reportInfoList.add(mrp);


                        JSONArray vd =job.getJSONArray("viewDetails");
                        for (int j = 0; j <vd.length() ; j++) {
                            JSONObject jsonObject1=vd.getJSONObject(j);
                            String downloadreport=jsonObject1.getString("downloadreport");

                            DownloadReportPJ drp=new DownloadReportPJ(downloadreport);
                            DownloadReportList.add(drp);
                        }
                    }
                    ReportInfoAdapter rpAdapter=new ReportInfoAdapter(MyReports.this,reportInfoList,DownloadReportList);
                    reportInfoRecyclerView.setAdapter(rpAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
