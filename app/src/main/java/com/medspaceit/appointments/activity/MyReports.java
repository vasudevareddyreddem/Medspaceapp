package com.medspaceit.appointments.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medspaceit.appointments.R;
import com.medspaceit.appointments.adapters.ReportInfoAdapter;
import com.medspaceit.appointments.apis.ApiUrl;
import com.medspaceit.appointments.model.ViewAllMyOrdersPojo;
import com.medspaceit.appointments.model.SuccessPaymentDetails;
import com.medspaceit.appointments.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

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
    getAllOrders();
    showDialog();
}else
    {showToast(getString(R.string.nointernet));}   }



    public  void getAllOrders()
    {
        showDialog();
        String json = "";
        String UID = manager.getSingleField(SessionManager.KEY_ID);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("a_u_id", UID);


        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.DIAGONOSTIC_BASE_URL + ApiUrl.orders_list, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();

                            Gson gson=new Gson();
                            ViewAllMyOrdersPojo data=gson.fromJson(response.toString(), ViewAllMyOrdersPojo.class);


                            if (data.status == 1) {
                                String UID = manager.getSingleField(SessionManager.KEY_ID);

                                ReportInfoAdapter rpAdapter=new ReportInfoAdapter(MyReports.this,data,UID);
                                reportInfoRecyclerView.setAdapter(rpAdapter);

                            } else {
                                showToast(data.message);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();

                }
            });
            RequestQueue queue = Volley.newRequestQueue(MyReports.this);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;}
    }
}
