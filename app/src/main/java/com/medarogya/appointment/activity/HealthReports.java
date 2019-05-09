package com.medarogya.appointment.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.medarogya.appointment.R;
import com.medarogya.appointment.adapters.AcceptAdapter;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.AcceptListPJ;
import com.medarogya.appointment.utils.MessageToast;
import com.medarogya.appointment.utils.SessionManager;
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



    List<AcceptListPJ> acceptList;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    public static final int MULTIPLE_PERMISSIONS = 11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_reports);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        checkPermissions();
        acceptListRcView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if (isConnected()) {
            showDialog();
            fetchAcceptStatusList();
        } else
            showToast("No Internet");
    }
    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(HealthReports.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
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
                            Log.e("4444444",response.toString());
                            acceptList=new ArrayList();
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
                                         String hos_id =js.getString("hos_id");
                                         String department =js.getString("department");
                                         String hospitalName =js.getString("hos_bas_name");
                                         String patinet_name =js.getString("patinet_name");
                                         AcceptListPJ acceptListPJ=new AcceptListPJ(date,time,department,hospitalName,hos_id,patinet_name);
                                         acceptList.add(acceptListPJ);
                                     }

                                     AcceptAdapter acceptAdapter=new AcceptAdapter(HealthReports.this,acceptList,manager,service);
                                     acceptListRcView.setAdapter(acceptAdapter);
                                 }
                                 else if(status.equals("0"))
                                 {
                                     String message=job.getString("message");
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
    }


}
