package com.medarogya.appointment.activity;

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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medarogya.appointment.R;
import com.medarogya.appointment.adapters.OPListAdapter;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.OPListPojo;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OPActivity extends BaseActivity {
String type,UID;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.op_rcview)
    RecyclerView op_rcview;

    @BindView(R.id.op_tot_bal)
    TextView op_tot_bal;
    @BindView(R.id.op_used_bal)
    TextView op_used_bal;
    @BindView(R.id.op_remaining_bal)
    TextView op_remaining_bal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_op);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        op_rcview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Intent i=getIntent();
        Bundle b=i.getExtras();
        type=b.getString("type");
        UID=b.getString("UID");

        if(isConnected()) {
            showDialog();
            fetchOPList(type,UID);
        }else {
            showToast(getString(R.string.nointernet));
        }
    }

    private void fetchOPList(String type, final String UID)
    {
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("a_u_id", UID);
          //  jsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.WalletBaseUrl + ApiUrl.op, new JSONObject(json),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Gson gson = new Gson();
                            OPListPojo data = gson.fromJson(String.valueOf(response), OPListPojo.class);
                         //Log.e("=======>",data.status.toString());
                            if (data.status!= 0) {

                                if (data.list != null) {
                                    OPListAdapter apAdapter = new OPListAdapter(OPActivity.this, data,UID);
                                    op_rcview.setAdapter(apAdapter);
                                    //TODO: using this on scroll data will not change
                                    op_rcview.setItemViewCacheSize(data.list.size());
                                    op_tot_bal.setText("Total Balance:\n Rs "+data.totalbalance);
                                    op_used_bal.setText("Used:\n Rs "+data.usedbalanceamount);
                                    op_remaining_bal.setText("Remaining:\n Rs "+data.remainingwalletamount);

                                } else {
                                    showToast(data.message);
                                }
                            }
                            else {
                                op_tot_bal.setText("Total Balance:\n Rs 0");
                                op_used_bal.setText("Used:\n Rs 0");
                                op_remaining_bal.setText("Remaining:\n Rs 0");

                                showToast(data.message);

                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                }
            });
            RequestQueue queue = Volley.newRequestQueue(OPActivity.this);
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
