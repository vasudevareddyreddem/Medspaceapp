package com.medspaceit.appointments.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.medspaceit.appointments.apis.ApiUrl;
import com.medspaceit.appointments.model.PatientDetailsPojo;
import com.medspaceit.appointments.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PatientDetails extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.et_patient_name)
    EditText et_patient_name;

    @BindView(R.id.et_patient_number)
    EditText et_patient_number;

    @BindView(R.id.et_patient_age)

    EditText et_patient_age;

    @BindView(R.id.et_patient_email)
    EditText et_patient_email;

    @BindView(R.id.rd_patient_gender)
    RadioGroup rd_patient_gender;

    @BindView(R.id.u_male)
    RadioButton u_male;


    @BindView(R.id.u_female)
    RadioButton u_female;


    @BindView(R.id.u_others)
    RadioButton u_others;


    @BindView(R.id.btn_next_b)
    Button btn_next_b;
    @BindView(R.id.your_state_progress_bar_id2)
    StateProgressBar your_state_progress_bar_id2;


    String gender = "";
    String udate, utime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        ButterKnife.bind(this);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        udate = b.getString("udate");
        utime = b.getString("utime");


        back.setOnClickListener(this);
        btn_next_b.setOnClickListener(this);
        rd_patient_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.u_male) {
                    gender = "Male";
                } else if (checkedId == R.id.u_female) {
                    gender = "Female";
                } else if (checkedId == R.id.u_others) {
                    gender = "Others";
                } else {
                    gender = "";
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                break;
            case R.id.btn_next_b:
                if (et_patient_name.getText().toString().equals("")) {
                    showToast("Please enter name");
                } else if (et_patient_number.getText().toString().equals("")) {
                    showToast("Please enter number");
                } else if (et_patient_number.getText().toString().length() < 10) {
                    showToast("Please enter correct number");
                } else if (et_patient_age.getText().toString().equals("")) {
                    showToast("Please enter age");
                } else if (et_patient_email.getText().toString().equals("")) {
                    showToast("Please enter email");
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(et_patient_email.getText().toString()).matches()) {
                    showToast("Please enter valid email");

                } else if (gender.equals("")) {
                    showToast("Please select gender");
                } else {

                    sendAllPatientDetails();
                }
                break;
        }
    }

    private void sendAllPatientDetails() {
        showDialog();
        String json = "";
        JSONObject jsonObject = new JSONObject();
        String UID = manager.getSingleField(SessionManager.KEY_ID);
        try {
            jsonObject.put("a_u_id", UID);
            jsonObject.put("time", utime);
            jsonObject.put("date", udate);
            jsonObject.put("gender", gender);
            jsonObject.put("name", et_patient_name.getText().toString());
            jsonObject.put("mobile", et_patient_number.getText().toString());
            jsonObject.put("age", et_patient_age.getText().toString());
            jsonObject.put("email", et_patient_email.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.DIAGONOSTIC_BASE_URL + ApiUrl.patientdetails, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();

                            Gson gson = new Gson();

                            PatientDetailsPojo patientData = gson.fromJson(response.toString(), PatientDetailsPojo.class);
                            if (patientData.status == 1) {
                                showToast(patientData.message);
                                Intent i = new Intent(PatientDetails.this, PickUpAddress.class);
                                i.putExtra("patient_details_id", patientData.patientDetailsId);
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
                    Log.e("Info crt 2", " Error " + error.getMessage());

                }
            });
            RequestQueue queue = Volley.newRequestQueue(PatientDetails.this);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Info crt 3", "Error  try " + e.getMessage());
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }
}
