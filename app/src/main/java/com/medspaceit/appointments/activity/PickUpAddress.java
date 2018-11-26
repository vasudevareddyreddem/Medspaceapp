package com.medspaceit.appointments.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.medspaceit.appointments.R;
import com.medspaceit.appointments.adapters.BillingAdderssAdapter;
import com.medspaceit.appointments.apis.ApiUrl;
import com.medspaceit.appointments.model.PatientBillingAddressPojo;
import com.medspaceit.appointments.model.BillingAddressListPojo;
import com.medspaceit.appointments.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickUpAddress extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.labelSpinner)
    Spinner labelSpinner;

    @BindView(R.id.et_home_address)
    EditText et_home_address;

    @BindView(R.id.et_landmark)
    EditText et_landmark;

    @BindView(R.id.billing_address_rcview)
    RecyclerView billing_address_rcview;

    @BindView(R.id.et_city)
    EditText et_city;

    @BindView(R.id.et_pincode)
    EditText et_pincode;
@BindView(R.id.qqqqq)
TextView qqqqq;


    @BindView(R.id.btn_next_c)
    Button btn_next_c;
    @BindView(R.id.your_state_progress_bar_id3)
    StateProgressBar your_state_progress_bar_id3;

    String patient_details_id,passAmount,pname,pnumber,pemail;
    String[] arraySpinner = new String[] {
            "Select Label", "Home", "Work", "Other"
    };

    String label;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up_address);
        ButterKnife.bind(this);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        billing_address_rcview.setLayoutManager(llm);
        patient_details_id = b.getString("patient_details_id");
        passAmount = b.getString("passAmount");
        pname = b.getString("pname");
        pnumber = b.getString("pnumber");
        pemail = b.getString("pemail");

        back.setOnClickListener(this);
        btn_next_c.setOnClickListener(this);
        qqqqq.setFocusable(true);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        labelSpinner.setAdapter(adapter);


        if(isConnected()) {
            getbillingAddressList();
            showDialog();
        }else
        {showToast(getString(R.string.nointernet));}   }

    private void getbillingAddressList() {


        showDialog();
        String json = "";
        JSONObject jsonObject = new JSONObject();
        String UID = manager.getSingleField(SessionManager.KEY_ID);
        try {
            jsonObject.put("a_u_id", UID);

        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.DIAGONOSTIC_BASE_URL + ApiUrl.address_list, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();

                            Gson gson = new Gson();

                            BillingAddressListPojo billingData = gson.fromJson(response.toString(), BillingAddressListPojo.class);
                            if (billingData.status == 1) {
                                String UID = manager.getSingleField(SessionManager.KEY_ID);
                                billing_address_rcview.setVisibility(View.VISIBLE);
                                BillingAdderssAdapter adderssAdapter=new BillingAdderssAdapter(PickUpAddress.this,billingData,UID,patient_details_id,passAmount,pname,pnumber,pemail);
                                billing_address_rcview.setAdapter(adderssAdapter);

                            } else {
                                billing_address_rcview.setVisibility(View.GONE);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();

                }
            });
            RequestQueue queue = Volley.newRequestQueue(PickUpAddress.this);
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
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                break;
            case R.id.btn_next_c:
                label = labelSpinner.getSelectedItem().toString();
                if (et_home_address.getText().toString().equals("")) {
                    showToast("Please enter Address");
                } else if (et_landmark.getText().toString().equals("")) {
                    showToast("Please enter Landmark");
                }  else if (et_city.getText().toString().equals("")) {
                    showToast("Please enter City");
                }
                else if (et_pincode.getText().toString().equals("")) {
                    showToast("Please enter Pincode");
                }
                else if (et_pincode.getText().toString().length() < 6) {
                    showToast("Please enter valid Pincode");
                }
                else if (label.equals("Select Label")) {
                    showToast("Please select Label");
                }
                else {
                    if(isConnected())
                    sendPatientBillingAddress();
                     else
                        showToast(getString(R.string.nointernet));
                }
                break;


        }
    }

    private void sendPatientBillingAddress() {

        showDialog();
        String json = "";
        JSONObject jsonObject = new JSONObject();
        String UID = manager.getSingleField(SessionManager.KEY_ID);
        try {
            jsonObject.put("a_u_id", UID);
            jsonObject.put("locality", et_city.getText().toString());
            jsonObject.put("address", et_home_address.getText().toString());
            jsonObject.put("pincode", et_pincode.getText().toString());
            jsonObject.put("landmark", et_landmark.getText().toString());
            jsonObject.put("address_lable", label);
              } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.DIAGONOSTIC_BASE_URL + ApiUrl.billing_address, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();

                            Gson gson = new Gson();

                            PatientBillingAddressPojo billingData = gson.fromJson(response.toString(), PatientBillingAddressPojo.class);
                            if (billingData.status == 1) {
                                showToast(billingData.message);
                                Intent i = new Intent(PickUpAddress.this, ReviewTests.class);

                                i.putExtra("patient_details_id",patient_details_id);
                                i.putExtra("billing_id",billingData.billingId.toString());
                                i.putExtra("passAmount",passAmount);
                                i.putExtra("pemail",pemail);
                                i.putExtra("pname",pname);
                                i.putExtra("pnumber",pnumber);
                                startActivity(i);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                            } else {
                                showToast(billingData.message);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();

                }
            });
            RequestQueue queue = Volley.newRequestQueue(PickUpAddress.this);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }}
