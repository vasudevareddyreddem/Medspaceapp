package com.medspaceit.appointments.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.medspaceit.appointments.apis.ApiUrl;
import com.medspaceit.appointments.model.PatientBillingAddressPojo;
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

    @BindView(R.id.et_city)
    EditText et_city;

    @BindView(R.id.et_pincode)
    EditText et_pincode;


    @BindView(R.id.btn_next_c)
    Button btn_next_c;
    @BindView(R.id.your_state_progress_bar_id3)
    StateProgressBar your_state_progress_bar_id3;

    String patient_details_id,passAmount;
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

        patient_details_id = b.getString("patient_details_id");
        passAmount = b.getString("passAmount");
        back.setOnClickListener(this);
        btn_next_c.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        labelSpinner.setAdapter(adapter);




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
                        showToast(getString(R.string.nointernet));                      }
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

                            PatientBillingAddressPojo patientData = gson.fromJson(response.toString(), PatientBillingAddressPojo.class);
                            if (patientData.status == 1) {
                                showToast(patientData.message);
                                Intent i = new Intent(PickUpAddress.this, ReviewTests.class);
                                i.putExtra("patient_details_id",patient_details_id);
                                i.putExtra("billing_id",patientData.billingId);
                                i.putExtra("passAmount",passAmount);
                                startActivity(i);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                            } else {
                                showToast(patientData.message);
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
