package com.medarogya.appointment.activity;

import android.content.Intent;
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
import com.medarogya.appointment.adapters.WalletHistoryAdapter;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.WalletHistoryPojo;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletHistory extends BaseActivity {
    String type,UID;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.wallethistory_rcview)
    RecyclerView wallethistory_rcview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_history);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        wallethistory_rcview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Intent i=getIntent();
        Bundle b=i.getExtras();
        UID=b.getString("UID");

        if(isConnected()) {
            showDialog();
            fetchWalletHistory(UID);
        }else {
            showToast(getString(R.string.nointernet));
        }
    }

    private void fetchWalletHistory(final String UID)
    {
        String json = "";
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
                    ApiUrl.WalletBaseUrl + ApiUrl.labhistory, new JSONObject(json),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Gson gson = new Gson();
                            WalletHistoryPojo data = gson.fromJson(String.valueOf(response), WalletHistoryPojo.class);
                            if (data.status!= 0) {

                                if (data.list != null) {
                                    WalletHistoryAdapter apAdapter = new WalletHistoryAdapter(WalletHistory.this, data,UID);
                                    wallethistory_rcview.setAdapter(apAdapter);
                                    //TODO: using this on scroll data will not change
                                    wallethistory_rcview.setItemViewCacheSize(data.list.size());

                                } else {
                                    showToast(data.message);
                                }
                            }
                            else {
                                showToast(data.message);

                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                }
            });
            RequestQueue queue = Volley.newRequestQueue(WalletHistory.this);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();
            hideDialog();
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
