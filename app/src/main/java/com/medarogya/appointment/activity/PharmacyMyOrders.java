package com.medarogya.appointment.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medarogya.appointment.R;
import com.medarogya.appointment.adapters.PharmacyMyReportAdapter;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.PharmacyMyReportPojo;
import com.medarogya.appointment.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PharmacyMyOrders extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_trackheader)
    TextView tv_trackheader;
    @BindView(R.id.rv_pharmacyreport)
    RecyclerView rv_pharmacyreport;

    String UID,track;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_my_orders);
        ButterKnife.bind(this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_pharmacyreport.setLayoutManager(llm);
        back.setOnClickListener(this);
        Bundle b=getIntent().getExtras();
        track=b.getString("track");
        if(track.equals("1"))
            tv_trackheader.setText("Track Order List");
        else
            tv_trackheader.setText("My Orders");


        UID = manager.getSingleField(SessionManager.KEY_ID);

        if (isConnected()) {
            showDialog();
            getAllReports();

        } else {
            showToast(getString(R.string.nointernet));
        }
    }

    private void getAllReports() {
        showDialog();
        String json="";
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("user_id",UID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json=jsonObject.toString();
        JsonObjectRequest jsonObjectRequest=null;

        try {
            jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.PHARMACYUrl + ApiUrl.orderslist, new JSONObject(json), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                        hideDialog();
                     Gson gson=new Gson();
                    PharmacyMyReportPojo data=gson.fromJson(response.toString(), PharmacyMyReportPojo.class);
                    if(data.status==0)
                    {
                        showToast(data.message.toString());
                    }
                    else {
                        PharmacyMyReportAdapter adapter=new PharmacyMyReportAdapter(PharmacyMyOrders.this,data,track);
                        rv_pharmacyreport.setAdapter(adapter);
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                    showToast(getResources().getString(R.string.serverproblem));

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            hideDialog();
            showToast(getResources().getString(R.string.serverproblem));

        }
        RequestQueue queue = Volley.newRequestQueue(PharmacyMyOrders.this);
        queue.add(jsonObjectRequest);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
        }

    }
}
