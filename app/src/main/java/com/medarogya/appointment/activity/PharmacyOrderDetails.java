package com.medarogya.appointment.activity;

import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medarogya.appointment.R;
import com.medarogya.appointment.adapters.PharmacyMyReportDetailsAdapter;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.PharmacyMyReportDetailsPojo;
import com.medarogya.appointment.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PharmacyOrderDetails extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.rv_pharmacyreportdetails)
    RecyclerView rv_pharmacyreportdetails;
   String phar_id,UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_order_details);

        ButterKnife.bind(this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_pharmacyreportdetails.setLayoutManager(llm);
        back.setOnClickListener(this);
        UID = manager.getSingleField(SessionManager.KEY_ID);
        Bundle b=getIntent().getExtras();
        phar_id=b.getString("phar_id");

        if (isConnected()) {
            showDialog();

            getAllReportDetails();
        } else {
            showToast(getString(R.string.nointernet));
        }
    }

    private void getAllReportDetails() {
        showDialog();
        String json="";
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("user_id",UID);
            jsonObject.put("id",phar_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json=jsonObject.toString();
        JsonObjectRequest jsonObjectRequest=null;

        try {
            jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.PHARMACYUrl + ApiUrl.orders_details, new JSONObject(json), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    hideDialog();
                    Gson gson=new Gson();
                    PharmacyMyReportDetailsPojo data=gson.fromJson(response.toString(), PharmacyMyReportDetailsPojo.class);
                    if(data.status==0)
                    {
                        showToast(data.message.toString());
                    }
                    else {
                        PharmacyMyReportDetailsAdapter adapter=new PharmacyMyReportDetailsAdapter(PharmacyOrderDetails.this,data);
                        rv_pharmacyreportdetails.setAdapter(adapter);
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
        RequestQueue queue = Volley.newRequestQueue(PharmacyOrderDetails.this);
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
