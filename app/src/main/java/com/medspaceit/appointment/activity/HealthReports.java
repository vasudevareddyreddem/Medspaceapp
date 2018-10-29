package com.medspaceit.appointment.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medspaceit.appointment.R;
import com.medspaceit.appointment.adapters.AcceptAdapter;
import com.medspaceit.appointment.adapters.StatusAdapter;
import com.medspaceit.appointment.apis.ApiUrl;
import com.medspaceit.appointment.model.AcceptListPJ;
import com.medspaceit.appointment.model.AppStatus;
import com.medspaceit.appointment.utils.MessageToast;
import com.medspaceit.appointment.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HealthReports extends BaseActivity {
    @BindView(R.id.acceptListRcView)
    RecyclerView acceptListRcView;
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.activity_title)
    TextView title;

    List<AcceptListPJ> acceptList;
//
//    HealthReportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_reports);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        acceptListRcView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if (isConnected()) {
            showDialog();
            fetchAcceptStatusList();
        } else
            MessageToast.showToastMethod(this, "No Internet");



    }

    private void fetchAcceptStatusList() {

        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("a_u_id", manager.getSingleField(SessionManager.KEY_ID));

        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.BaseUrl+ApiUrl.acceptstatuslist, new JSONObject(json),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            acceptList=new ArrayList();
                            Log.d("response=======",response.toString());
                            // {"status":1,"card_assign_number":21,"a_u_id":"mn2","message":"Card Number successfully added"}
                            try {
                                JSONObject job=new JSONObject(String.valueOf(response));

                                String status=job.getString("status");

                                 if(status.equals("1"))
                                 {

                                     JSONArray jsonArray=job.getJSONArray("list");
                                     for (int i = 0; i < jsonArray.length(); i++) {
                                         JSONObject js=jsonArray.getJSONObject(i);
                                         String date=js.getString("date");
                                         String time =js.getString("time");
                                         String department =js.getString("department");
                                         String hospitalName =js.getString("hos_bas_name");
                                         AcceptListPJ acceptListPJ=new AcceptListPJ(date,time,department,hospitalName);
                                         acceptList.add(acceptListPJ);
                                     }

                                     AcceptAdapter acceptAdapter=new AcceptAdapter(HealthReports.this,acceptList);
                                     acceptListRcView.setAdapter(acceptAdapter);
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
        finish();
    }
}
