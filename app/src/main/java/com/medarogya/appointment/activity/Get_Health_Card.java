package com.medarogya.appointment.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.medarogya.appointment.R;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.utils.MessageToast;
import com.medarogya.appointment.utils.SessionManager;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;

public class Get_Health_Card extends BaseActivity implements View.OnClickListener, PaymentResultListener {
    @BindView(R.id.Submit_card)
    Button Submit_card;

    @BindView(R.id.pleaseWaitLayout)
    LinearLayout pleaseWaitLayout;

    @BindView(R.id.mainLayoutGHC)
    LinearLayout mainLayoutGHC;

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

    String amount;
    String card_assign_number;
    double total;

    public static Activity ghc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get__health__card);
        ghc = this;
        ButterKnife.bind(this);

        back.setOnClickListener(this);
        Submit_card.setOnClickListener(this);
        RadioGroup rg = (RadioGroup) findViewById(R.id.radio_group);
        hideSoftKeyboard();
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
                        String ans = phone.substring(0, 4) + " " + phone.substring(4, 8) + " " + phone.substring(8, 12);
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
            apiGetCardAmount();
        } else showToast("No Internet");


    }

    private void apiGetCardAmount() {
        final StringRequest request = new StringRequest(Request.Method.POST, ApiUrl.BaseUrl + ApiUrl.cardamount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    amount = jsonObject.getString("amount");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
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
                hideSoftKeyboard();
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
            jsonObject.put("a_u_id", manager.getSingleField(SessionManager.KEY_ID));
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
                    ApiUrl.BaseUrl + ApiUrl.takecardnumber, new JSONObject(json),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();

                            // {"status":1,"card_assign_number":21,"a_u_id":"mn2","message":"Card Number successfully added"}
                            try {
                                JSONObject job = new JSONObject(String.valueOf(response));
                                String message = job.getString("message");
                                String status = job.getString("status");
                                if (status.equals("0")) {
                                    MessageToast.showToastMethod(Get_Health_Card.this, message);

                                } else {
                                    hideSoftKeyboard();
                                    card_assign_number = job.getString("card_assign_number");
                                    mainLayoutGHC.setVisibility(View.GONE);
                                    pleaseWaitLayout.setVisibility(View.VISIBLE);
                                    Toast.makeText(Get_Health_Card.this, message, Toast.LENGTH_SHORT).show();

                                    if (isConnected()) {

                                        startPayment();

                                    } else
                                        showToast(getString(R.string.nointernet));



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

    public void check_no(View view) {

        String molibe_num = molibe_number.getText().toString();
        whatsapp_number.setText(molibe_num);
        whatsapp_number.setError(null);
        if (check_same_num.isChecked()) {

        } else {
            whatsapp_number.setText("");
        }


    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void startPayment() {
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", pt_name.getText().toString());
            options.put("description", "Health Card Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");


            total = Double.parseDouble(amount);
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email.getText().toString());
            preFill.put("contact", molibe_number.getText().toString());

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            mainLayoutGHC.setVisibility(View.VISIBLE);
            pleaseWaitLayout.setVisibility(View.GONE);
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {


        CallApi(razorpayPaymentID);
    }

    private void CallApi(String razorpayPaymentID) {

        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("a_u_id", manager.getSingleField(SessionManager.KEY_ID));
            jsonObject.put("razorpay_payment_id", razorpayPaymentID);
            jsonObject.put("card_assign_number", card_assign_number);
            jsonObject.put("amount", total);

        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;
        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.BaseUrl + ApiUrl.payment, new JSONObject(json),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            try {
                                JSONObject job = new JSONObject(String.valueOf(response));
                                String message = job.getString("message");
                                Toast.makeText(Get_Health_Card.this, message, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Get_Health_Card.this, FinalScreen.class);
                                intent.putExtra("name", pt_name.getText().toString());
                                intent.putExtra("phone", molibe_number.getText().toString());
                                intent.putExtra("heaith_card_no", heaith_card_no.getText().toString());
                                startActivity(intent);
                                heaith_card_no.setText("");
                                molibe_number.setText("");
                                pt_city.setText("");
                                whatsapp_number.setText("");
                                email.setText("");


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                    mainLayoutGHC.setVisibility(View.VISIBLE);
                    pleaseWaitLayout.setVisibility(View.GONE);

                }
            });
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentError(int code, String response) {
        mainLayoutGHC.setVisibility(View.VISIBLE);
        pleaseWaitLayout.setVisibility(View.GONE);
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }

}
