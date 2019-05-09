package com.medarogya.appointment.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medarogya.appointment.R;
import com.medarogya.appointment.adapters.PharmacyListAdapter;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.CityResponces;
import com.medarogya.appointment.model.Formatter;
import com.medarogya.appointment.model.Message;

import com.medarogya.appointment.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderMedicines extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.btn_call_orders)
    Button btn_call_orders;


    @BindView(R.id.up_city_ll)
    LinearLayout up_city_ll;
    @BindView(R.id.up_city_txt)
    TextView up_city_txt;
    @BindView(R.id.rcv_pharmacy)
    RecyclerView rcv_pharmacy;


    List<Message> cityList = null;


    String city;
    int precitypos = -1;
    int prepharmacypos = -1;

    String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_medicines);
        ButterKnife.bind(this);
        UID = manager.getSingleField(SessionManager.KEY_ID);
        back.setOnClickListener(this);
        btn_call_orders.setOnClickListener(this);
        rcv_pharmacy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        if (isConnected()) {
            showDialog();
            FetchCity();

        } else {
            showToast("No Internet Connection Found");
        }
    }


    private void FetchCity() {

        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", UID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json = jsonObject.toString();
        JsonObjectRequest jsonObjectRequest = null;

        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.PHARMACYUrl + ApiUrl.get_cities, new JSONObject(json), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    hideDialog();
                    Gson gson = new Gson();
                    CityResponces data = gson.fromJson(response.toString(), CityResponces.class);
                    if (data.getStatus() == 0) {
                        showToast("City list not found!");                    }

                    else {

                        cityList = data.message;
                    }
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();


                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            hideDialog();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        assert jsonObjectRequest != null;
        requestQueue.add(jsonObjectRequest);
    }

    int data_type = 0;

    public void openSingleChoice(View view) {

        List list = null;
        String title = null;
        switch (view.getId()) {
            case R.id.up_city_ll:
                list = cityList;
                title = "Select City";
                data_type = 1;
                break;

        }

        if (list != null) {
            final String[] items = getStringArray(list);
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
            View view1 = getLayoutInflater().inflate(R.layout.spinner_item, null);
            TextView mTitle = view1.findViewById(R.id.txt_item);
            mTitle.setText(title);
            switch (data_type) {
                case 1:
                    builder.setCustomTitle(view1)
                            .setSingleChoiceItems(items, precitypos, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    fetchData(data_type, which);
                                    data_type = 0;


                                }
                            });
                    break;

            }
            builder.create().show();
        }
    }

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

                city = cityList.get(position).getValue();
                up_city_txt.setText(city);
                FetchPharmacyList(city);
                precitypos = position;
                prepharmacypos = -1;
                showDialog();


                break;

        }
    }

    private void FetchPharmacyList(String city) {

        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", UID);
            jsonObject.put("city", city);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json = jsonObject.toString();
        JsonObjectRequest jsonObjectRequest = null;

        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.PHARMACYUrl + ApiUrl.get_pharmcies, new JSONObject(json), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    hideDialog();
                    Gson gson = new Gson();
                    PharmacyUploadPic data = gson.fromJson(String.valueOf(response), PharmacyUploadPic.class);
                    if (data.status == 1) {

                        PharmacyListAdapter adapter=new PharmacyListAdapter(OrderMedicines.this,data);
                        rcv_pharmacy.setAdapter(adapter);
                    } else {
                        showToast("No Pharmacy Availables");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            hideDialog();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;


            case R.id.btn_call_orders:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:7997999108"));
                startActivity(intent);
                break;
        }
    }


}