package com.medspaceit.appointments.activity;

import android.content.Intent;
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
import com.medspaceit.appointments.R;
import com.medspaceit.appointments.adapters.MyReportDownloadAdapter;
import com.medspaceit.appointments.apis.ApiUrl;
import com.medspaceit.appointments.model.MyDiagnosticReportDownloadPojo;
import com.medspaceit.appointments.model.ViewAllMyOrdersPojo;
import com.medspaceit.appointments.utils.MessageToast;
import com.medspaceit.appointments.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyDiagnosticReportDownload extends BaseActivity {
String UID,order_item_id;
    @BindView(R.id.downloadReportRCView)
    RecyclerView downloadReportRCView;
    @BindView(R.id.back)
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_diagnostic_report_download);

        ButterKnife.bind(this);
        back.setOnClickListener(this);
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        UID=b.getString("UID");
        order_item_id=b.getString("order_item_id");

        downloadReportRCView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if (isConnected()) {
            showDialog();
            fetchReports();
        } else
            MessageToast.showToastMethod(this, "No Internet");


    }

    private void fetchReports() {

        showDialog();
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("a_u_id", UID);
            jsonObject.put("order_item_id", order_item_id);
                    } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.DIAGONOSTIC_BASE_URL + ApiUrl.download_reports, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();

                            Gson gson = new Gson();

                            MyDiagnosticReportDownloadPojo reportData = gson.fromJson(response.toString(), MyDiagnosticReportDownloadPojo.class);
                            if (reportData.status == 1) {

                                MyReportDownloadAdapter adapter=new MyReportDownloadAdapter(MyDiagnosticReportDownload.this, reportData);
                                downloadReportRCView.setAdapter(adapter);
                            } else {
                                showToast(reportData.message);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();

                }
            });
            RequestQueue queue = Volley.newRequestQueue(MyDiagnosticReportDownload.this);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    @Override
    public void onClick(View v) {

    }
}
