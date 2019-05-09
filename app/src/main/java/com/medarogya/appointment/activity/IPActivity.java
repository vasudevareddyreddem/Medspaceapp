package com.medarogya.appointment.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.medarogya.appointment.R;
import com.medarogya.appointment.adapters.IPListAdapter;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.City;
import com.medarogya.appointment.model.CityList;
import com.medarogya.appointment.model.Formatter;
import com.medarogya.appointment.model.Hospital;
import com.medarogya.appointment.model.HospitalList;
import com.medarogya.appointment.model.IPListPojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IPActivity extends BaseActivity {
    String type,UID;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.Ip_rcview)
    RecyclerView ip_rcview;
    @BindView(R.id.ip_city_ll)
    LinearLayout ip_city_ll;
    @BindView(R.id.ip_city_txt)
    TextView ip_city_txt;

    @BindView(R.id.ip_hos_ll)
    LinearLayout ip_hos_ll;
    @BindView(R.id.ip_hos_txt)
    TextView ip_hos_txt;
    @BindView(R.id.ip_lab_header)
    TextView ip_lab_header;

    @BindView(R.id.btn_generate_coupon)
    Button btn_generate_coupon;
    @BindView(R.id.ip_tot_bal)
    TextView ip_tot_bal;
    @BindView(R.id.ip_used_bal)
    TextView ip_used_bal;
    @BindView(R.id.ip_remaining_bal)
    TextView ip_remaining_bal;
    List<City> cities = null;
    List<Hospital> hospitals = null;

    String city,hospital,hos_id,APIURLMAIN,APIURLCOUPEN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        btn_generate_coupon.setOnClickListener(this);
        ip_rcview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Intent i=getIntent();
        Bundle b=i.getExtras();

        UID=b.getString("UID");
        type=b.getString("type");
        if(type.equals("IP"))
        {
            ip_lab_header.setText(type);
            APIURLMAIN=ApiUrl.ip;
            APIURLCOUPEN=ApiUrl.generateipcoupon;
        }
        else if(type.equals("LAB"))
        {
            ip_lab_header.setText(type);
            APIURLMAIN=ApiUrl.lab;
            APIURLCOUPEN=ApiUrl.generatelabcoupon;

        }
        if(isConnected()) {

            FeatchCity();
            fetchOPList(UID);
        }else {
            showToast(getString(R.string.nointernet));
        }
    }

    private void FeatchCity() {
        Call<CityList> call = service.getCitys(new JsonObject(), ApiUrl.content_type);
        showDialog();
        call.enqueue(new Callback<CityList>() {
            @Override
            public void onResponse(Call<CityList> call, Response<CityList> response) {
                hideDialog();
                if (!response.isSuccessful()) {
                    showToast("Server side error");
                    return;
                }


                CityList cityList = response.body();

                if (cityList != null) {


                    if (cityList.getStatus() == 1) {
                        cities = cityList.getCitys();
                        Log.e("---------",cityList.getCitys().toString());
                    } else
                        showToast(cityList.getMessage());
                }


            }

            @Override
            public void onFailure(Call<CityList> call, Throwable t) {
                hideDialog();
            }
        });
    }

    private void FetchHospitals(String city) {
        JsonObject object = new JsonObject();

        object.addProperty("city", city);
        Call<HospitalList> call = service.getHospitals(object, ApiUrl.content_type);
        showDialog();
        call.enqueue(new Callback<HospitalList>() {
            @Override
            public void onResponse(Call<HospitalList> call, Response<HospitalList> response) {
                hideDialog();
                if (!response.isSuccessful()) {
                    showToast("Server side error");
                    return;
                }

                HospitalList detps = response.body();
                if (detps != null) {

                    if (detps.getStatus() == 1) {
                        hospitals = detps.getHospital();

                    } else {
                        ip_hos_txt.setText("No Hospital");
                        showToast(detps.getMessage());

                    }
                }

            }

            @Override
            public void onFailure(Call<HospitalList> call, Throwable t) {
                hideDialog();
            }

        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

                case R.id.btn_generate_coupon:
                if (city == null || city.isEmpty()) {
                    showToast("Please select City");
                    return;
                }
               else if (hospital == null || hospital.isEmpty()) {
                    showToast("Please select Hospital");
                    return;
                }
                else {

                    if(isConnected())
                    {
                        showDialog();
                        GenerateIPCoupen(UID,hos_id);}

                    else {
                        showToast("No Internet");
                    }
                }
                break;
        }
    }

    private void GenerateIPCoupen(final String UID, String hos_id) {
        String json = "";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("a_u_id", UID);
            jsonObject.put("hos_id", hos_id);

        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.WalletBaseUrl + APIURLCOUPEN, new JSONObject(json),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Gson gson = new Gson();
                            IPListPojo data = gson.fromJson(String.valueOf(response), IPListPojo.class);
                            if (data.status!= 0) {
                                showToast(data.message);

                                fetchOPList(UID);
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
            RequestQueue queue = Volley.newRequestQueue(IPActivity.this);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();
            hideDialog();
        }

    }

    int data_type = 0;
    public void openSingleChoice(View view) {
        List list = null;
        String title = null;
        switch (view.getId()) {
            case R.id.ip_city_ll:
                list = cities;
                title = "Select City";
                data_type = 1;
                break;
            case R.id.ip_hos_ll:
                title = "Select Hospital";
                list = hospitals;
                data_type = 2;
                break;
    }

        if (list != null) {
            final String[] items = getStringArray(list);
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
            View view1 = getLayoutInflater().inflate(R.layout.spinner_item, null);
            TextView mTitle = view1.findViewById(R.id.txt_item);
            mTitle.setText(title);
            builder.setCustomTitle(view1)
                    .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            fetchData(data_type, which);
                            data_type = 0;


                        }
                    });
            builder.create().show();
        }}
        private String[] getStringArray(List formatters) {
            String[] strings = new String[formatters.size()];
            for (int i = 0; i < formatters.size(); i++) {
                strings[i] = ((Formatter) formatters.get(i)).getValue();
            }
            return strings;
        }



    private void fetchData(int data_type, int position) {
        switch (data_type) {
            case 1:
                city = cities.get(position).getValue();
                ip_city_txt.setText(city);
                FetchHospitals(city);
                hospitals = null;
                hospital="";
                ip_hos_txt.setText("Select Hospital");
                break;
            case 2:
                hospital = hospitals.get(position).getValue();
                hos_id = hospitals.get(position).getId();
               ip_hos_txt.setText(hospital);

                break;}}


    private void fetchOPList(final String UID)
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
                    ApiUrl.WalletBaseUrl + APIURLMAIN, new JSONObject(json),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Gson gson = new Gson();
                            IPListPojo data = gson.fromJson(String.valueOf(response), IPListPojo.class);
                            if (data.status!= 0) {

                                if (data.list != null) {
                                    IPListAdapter apAdapter = new IPListAdapter(IPActivity.this, data,UID);
                                    ip_rcview.setAdapter(apAdapter);
                                    //TODO: using this on scroll data will not change
                                    ip_rcview.setItemViewCacheSize(data.list.size());

                                    ip_tot_bal.setText("Total Balance:\n Rs "+data.totalbalance);
                                    ip_used_bal.setText("Used:\n Rs "+data.usedbalanceamount);
                                    ip_remaining_bal.setText("Remaining:\n Rs "+data.remainingwalletamount);

                                } else {
                                    showToast("No Data");
                                }

                            }
                            else {
                                ip_tot_bal.setText("Total Balance:\n Rs 0");
                                ip_used_bal.setText("Used:\n Rs 0");
                                ip_remaining_bal.setText("Remaining:\n Rs 0");

                                showToast(data.message);

                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                }
            });
            RequestQueue queue = Volley.newRequestQueue(IPActivity.this);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();
            hideDialog();
        }
    }
}
