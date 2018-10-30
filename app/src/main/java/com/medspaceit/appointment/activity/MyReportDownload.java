package com.medspaceit.appointment.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.medspaceit.appointment.R;
import com.medspaceit.appointment.adapters.DownloadAdapter;
import com.medspaceit.appointment.apis.ApiUrl;
import com.medspaceit.appointment.model.DownloadListPJ;
import com.medspaceit.appointment.utils.MessageToast;
import com.medspaceit.appointment.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyReportDownload  extends BaseActivity {
    @BindView(R.id.downloadListRcView)
    RecyclerView downloadListRcView;
    @BindView(R.id.back)
    ImageView back;

String hos_id;

    List<DownloadListPJ> downloadList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reportdownload);

        ButterKnife.bind(this);
        back.setOnClickListener(this);
        Intent i=getIntent();
        Bundle b=i.getExtras();
        hos_id=b.getString("hos_id");
        downloadListRcView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if (isConnected()) {
            showDialog();
            fetchDownloadList();
        } else
            MessageToast.showToastMethod(this, "No Internet");


    }

    private void fetchDownloadList() {

        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("a_u_id", manager.getSingleField(SessionManager.KEY_ID));
            jsonObject.put("hos_id", hos_id);

        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.BaseUrl+ApiUrl.prescriptionlist, new JSONObject(json),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            downloadList=new ArrayList();

                            try {
                                JSONObject job=new JSONObject(String.valueOf(response));

                                String status=job.getString("status");
                                String message=job.getString("message");


                                if(status.equals("1"))
                                {
                                    String path=job.getString("path");
                                    JSONArray jsonArray=job.getJSONArray("list");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject js=jsonArray.getJSONObject(i);
                                        String prescription=js.getString("prescription");
                                        String created_at =js.getString("created_at");

                                        DownloadListPJ downloadListPJ=new DownloadListPJ(prescription,created_at);
                                        downloadList.add(downloadListPJ);
                                    }

                                    DownloadAdapter da=new DownloadAdapter(MyReportDownload.this,downloadList,path);
                                    downloadListRcView.setAdapter(da);
                                }
                                else {
                                    showToast(message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();

                }
            });
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        super.onBackPressed();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }}