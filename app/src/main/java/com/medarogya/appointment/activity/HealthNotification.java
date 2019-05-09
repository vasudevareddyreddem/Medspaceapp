package com.medarogya.appointment.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medarogya.appointment.R;
import com.medarogya.appointment.adapters.HealthcampAdapter;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.HealthCampNotification;
import com.medarogya.appointment.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HealthNotification extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.healthcampnoti_rv)
    RecyclerView healthcampnoti_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_notification);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        healthcampnoti_rv.setLayoutManager(new LinearLayoutManager(this));

        if (isConnected()) {
            getHealthCampCount();
        } else {
            showToast("No Internet Connection");
        }
    }

    private void getHealthCampCount() {


        String json = "";
        JSONObject jsonObject = new JSONObject();
        final String UID = manager.getSingleField(SessionManager.KEY_ID);
        try {
            jsonObject.put("user_id", UID);
        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.HEALTHCAMPS_BASE_URL + ApiUrl.get_health_camps_notifi, new JSONObject(json),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Gson gson = new Gson();

                            HealthCampNotification noti = gson.fromJson(response.toString(), HealthCampNotification.class);
                            if (noti.status == 1) {
                                if (noti.list.size() != 0) {
                                    HealthcampAdapter adapter = new HealthcampAdapter(HealthNotification.this, noti, UID);
                                    healthcampnoti_rv.setAdapter(adapter);
                                }

                            } else {
                                showToast("No Notification Available.");
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });
            RequestQueue queue = Volley.newRequestQueue(HealthNotification.this);
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
                break;
        }
    }
}
