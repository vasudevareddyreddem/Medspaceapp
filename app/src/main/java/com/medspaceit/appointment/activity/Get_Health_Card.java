package com.medspaceit.appointment.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.medspaceit.appointment.R;
import com.medspaceit.appointment.apis.ApiUrl;
import com.medspaceit.appointment.utils.MessageToast;
import com.medspaceit.appointment.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;

public class Get_Health_Card extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.Submit_card)
    Button Submit_card;

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.heaith_card_no)
    EditText heaith_card_no;

    @BindView(R.id.patient_name)
    EditText pt_name;

    @BindView(R.id.molibe_number)
    EditText molibe_number;

    @BindView(R.id.whatsapp_number)
    EditText whatsapp_number;

    @BindView(R.id.check_same_num)
    CheckBox check_same_num;

    @BindView(R.id.city)
    EditText pt_city;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.radio_group)
    RadioGroup radio_group;

    @BindView(R.id.male)
    RadioButton male;

    @BindView(R.id.female)
    RadioButton female;

    @BindView(R.id.others)
    RadioButton others;

    String gender = "";
    String Cardnumber;

    static final Pattern CODE_PATTERN = Pattern.compile("([0-9]{0,4})|([0-9]{4}-)+|([0-9]{0,4})+");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get__health__card);

        ButterKnife.bind(this);

        back.setOnClickListener(this);
        Submit_card.setOnClickListener(this);
        RadioGroup rg = (RadioGroup) findViewById(R.id.radio_group);

        heaith_card_no.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            private boolean backspacingFlag = false;
            private boolean editedFlag = false;
            private int cursorComplement;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                cursorComplement = s.length() - heaith_card_no.getSelectionStart();
                if (count > after) {
                    backspacingFlag = true;
                } else {
                    backspacingFlag = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);

            }

            @Override
            public synchronized void afterTextChanged(Editable s) {


                String string = s.toString();
                String phone = string.replaceAll("[^\\d]", "");

                if (!editedFlag) {

                    //example: 999999999 <- 6+ digits already typed
                    // masked: (999) 999-999
                    if (phone.length() >= 12 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = phone.substring(0, 4) + " " + phone.substring(4, 8) + " " + phone.substring(8, 12) ;
                                heaith_card_no.setText(ans);
                        heaith_card_no.setSelection(heaith_card_no.getText().length() - cursorComplement);

                    }
                } else {
                    editedFlag = false;
                }
            }
        });


        getCardNumber();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male) {
                    gender = "Male";
                } else if (checkedId == R.id.female) {
                    gender = "Female";
                } else if (checkedId == R.id.others) {
                    gender = "Others";
                } else {
                    gender = "";
                }

            }
        });
    }

    private void getCardNumber() {
        if (isConnected()) {
            showDialog();

            apiCall();
        } else showToast("No Internet");


    }

    private void apiCall() {
        final StringRequest request = new StringRequest(Request.Method.POST, ApiUrl.BaseUrl + ApiUrl.cardgenerator, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    Cardnumber = jsonObject.getString("Cardnumber");
                    heaith_card_no.setText(Cardnumber);
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
        queue.add(request);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.Submit_card:
                validate();
                break;

        }
    }


    public void validate() {
        String heaith_card_num = heaith_card_no.getText().toString();
        String patient_name = pt_name.getText().toString();
        String molibe_num = molibe_number.getText().toString();
        String whatsapp_num = whatsapp_number.getText().toString();

        String city = pt_city.getText().toString();
        String mail = email.getText().toString();
        if (heaith_card_num == null || heaith_card_num.isEmpty()) {
            heaith_card_no.setError("Enter name");

        } else if (patient_name == null || patient_name.isEmpty()) {
            pt_name.setError("Enter name");

        } else if (molibe_num == null || molibe_num.isEmpty()) {
            molibe_number.setError("Enter number");

        } else if (molibe_num.length() < 10) {
            molibe_number.setError("Must 10 numbers");

        } else if (whatsapp_num == null || whatsapp_num.isEmpty()) {
            whatsapp_number.setError("Enter number");

        } else if (whatsapp_num.length() < 10) {
            whatsapp_number.setError("Must 10 numbers");

        } else if (city == null || city.isEmpty()) {
            pt_city.setError("Enter City");

        } else if (mail == null || mail.isEmpty()) {
            email.setError("Enter email id");

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            email.setError("enter a valid email address");

        } else if (gender.equals("")) {
            Toast.makeText(this, "Please select Gender", Toast.LENGTH_SHORT).show();
        } else {

            takeCardNumber();
        }

    }

    private void takeCardNumber() {
        if (isConnected()) {
            showDialog();

            apiCallForCard();
        } else showToast("No Internet");

    }

    private void apiCallForCard() {

        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("a_u_id", manager.getSingleField(SessionManager.KEY_NAME));
            jsonObject.put("card_number", heaith_card_no.getText().toString());
            jsonObject.put("patient_name", pt_name.getText().toString());
            jsonObject.put("mobile_num", molibe_number.getText().toString());
            jsonObject.put("whatsapp_num", whatsapp_number.getText().toString());
            jsonObject.put("city", pt_city.getText().toString());
            jsonObject.put("email_id", email.getText().toString());
            jsonObject.put("gender", gender);
        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;
        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.BaseUrl+ApiUrl.takecardnumber, new JSONObject(json),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            String card_assign_number;

                           // {"status":1,"card_assign_number":21,"a_u_id":"mn2","message":"Card Number successfully added"}
                            try {
                                JSONObject job=new JSONObject(String.valueOf(response));
                                String message=job.getString("message");
                                String status=job.getString("status");
                                if(status.equals("0"))
                                {
                                    MessageToast.showToastMethod(Get_Health_Card.this,message);

                                }
                                else{
                                    finish();
                                    card_assign_number=job.getString("card_assign_number");

                                    MessageToast.showToastMethod(Get_Health_Card.this,message);

                                    Intent intent=new Intent(Get_Health_Card.this,CheckoutActivity.class);
                                    intent.putExtra("email",email.getText().toString());
                                    intent.putExtra("name",pt_name.getText().toString());
                                    intent.putExtra("phone",molibe_number.getText().toString());
                                    intent.putExtra("card_assign_number",card_assign_number);
                                    intent.putExtra("heaith_card_no",heaith_card_no.getText().toString());
                                    startActivity(intent);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                        Cardnumber = jsonObject.getString("Cardnumber");
//                        heaith_card_no.setText(Cardnumber);

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

    public void check_no(View view) {

        String molibe_num = molibe_number.getText().toString();
        whatsapp_number.setText(molibe_num);
        whatsapp_number.setError(null);
        if (check_same_num.isChecked()) {

        } else {
            whatsapp_number.setText("");
        }


    }
}
