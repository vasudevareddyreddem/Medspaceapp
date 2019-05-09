package com.medarogya.appointment.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medarogya.appointment.R;
import com.medarogya.appointment.adapters.AppliedListAdapter;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.AppliedJoblistPojo;
import com.medarogya.appointment.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppliedListActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    String UID;
    @BindView(R.id.rcv_jobappliedlist)
    RecyclerView rcv_jobappliedlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied_list);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_jobappliedlist.setLayoutManager(llm);
        UID = manager.getSingleField(SessionManager.KEY_ID);
        if (isConnected()) {
            showDialog();
            getAppliedList();

        } else {
            showToast("No Internet");

        }
    }

    private void getAppliedList() {
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
                    ApiUrl.PHARMACYUrl + ApiUrl.user_joblist, new JSONObject(json), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    hideDialog();
                    Gson gson=new Gson();
                    AppliedJoblistPojo data=gson.fromJson(String.valueOf(response),AppliedJoblistPojo.class);
                    if(data.status!=0) {
                        AppliedListAdapter adapter = new AppliedListAdapter(AppliedListActivity.this, data, UID);
                        rcv_jobappliedlist.setAdapter(adapter);
                    }
                    else {
                        showToast(data.message);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue= Volley.newRequestQueue(this);
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
