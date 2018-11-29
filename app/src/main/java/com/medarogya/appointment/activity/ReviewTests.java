package com.medarogya.appointment.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.medarogya.appointment.R;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.CODPaymentPojo;
import com.medarogya.appointment.model.SuccessPaymentDetails;
import com.medarogya.appointment.utils.SessionManager;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewTests extends BaseActivity implements PaymentResultListener {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.txt_famount)
    TextView txt_famount;

    @BindView(R.id.paymeny_type)
    RadioGroup paymeny_type;

    @BindView(R.id.cod)
    RadioButton cod;


    @BindView(R.id.online_payment)
    RadioButton online_payment;


    @BindView(R.id.btn_paynow)
    Button btn_paynow;

    @BindView(R.id.your_state_progress_bar_id4)
    StateProgressBar your_state_progress_bar_id4;

    String patient_details_id, billing_id, passAmount;
    String paymentType = "", pname, pnumber, pemail;
    double total;

    public static SuccessPaymentDetails successData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_tests);
        ButterKnife.bind(this);

        back.setOnClickListener(this);
        btn_paynow.setOnClickListener(this);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        patient_details_id = b.getString("patient_details_id");
        billing_id = b.getString("billing_id");
        passAmount = b.getString("passAmount");
        pname = b.getString("pname");
        pnumber = b.getString("pnumber");
        pemail = b.getString("pemail");
        txt_famount.setText("Total: ₹" + passAmount);


        paymeny_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                btn_paynow.setVisibility(View.VISIBLE);
                if (checkedId == R.id.online_payment) {

                    paymentType = "1";
                } else if (checkedId == R.id.cod) {
                    paymentType = "2";
                } else {
                    paymentType = "";
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

            case R.id.btn_paynow:

                if (paymentType.equals("1")) {
                    if (isConnected()) {

                        callOnlinePayment();

                    } else {
                        showToast(getString(R.string.nointernet));
                    }

                } else if (paymentType.equals("2")) {
                    if (isConnected()) {
                        callCashOnDelivery();
                    } else {
                        showToast(getString(R.string.nointernet));
                    }
                }


                break;

        }
    }

    public void callOnlinePayment() {
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", pname);
            options.put("description", "Lab test purchase");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");


            total = Double.parseDouble(passAmount);
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
            preFill.put("email", pemail);
            preFill.put("contact", pnumber);

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {

            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {


        CallApi(razorpayPaymentID);
    }

    private void CallApi(String razorpayPaymentID) {

        showDialog();
        String json = "";
        JSONObject jsonObject = new JSONObject();
        final String UID = manager.getSingleField(SessionManager.KEY_ID);
        try {
            jsonObject.put("a_u_id", UID);
            jsonObject.put("payment_type", paymentType);
            jsonObject.put("patient_details_id", patient_details_id);
            jsonObject.put("billing_id", billing_id);
            jsonObject.put("razorpay_payment_id", razorpayPaymentID);
            jsonObject.put("razorpay_order_id", "");
            jsonObject.put("razorpay_signature", "");
        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.DIAGONOSTIC_BASE_URL + ApiUrl.dgpayment, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();

                            Gson gson = new Gson();

                            CODPaymentPojo codData = gson.fromJson(response.toString(), CODPaymentPojo.class);
                            if (codData.status == 1) {

                                callCODSuccess(codData.orderId, UID);
                            } else {
                                showToast(codData.message);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();

                }
            });
            RequestQueue queue = Volley.newRequestQueue(ReviewTests.this);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();

        }


    }

    @Override
    public void onPaymentError(int code, String response) {

        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }


    private void callCashOnDelivery() {

        showDialog();
        String json = "";
        JSONObject jsonObject = new JSONObject();
        final String UID = manager.getSingleField(SessionManager.KEY_ID);
        try {
            jsonObject.put("a_u_id", UID);
            jsonObject.put("payment_type", paymentType);
            jsonObject.put("patient_details_id", patient_details_id);
            jsonObject.put("billing_id", billing_id);
            jsonObject.put("razorpay_payment_id", "");
            jsonObject.put("razorpay_order_id", "");
            jsonObject.put("razorpay_signature", "");
        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.DIAGONOSTIC_BASE_URL + ApiUrl.dgpayment, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();

                            Gson gson = new Gson();

                            CODPaymentPojo codData = gson.fromJson(response.toString(), CODPaymentPojo.class);
                            if (codData.status == 1) {

                                callCODSuccess(codData.orderId, UID);
                            } else {
                                showToast(codData.message);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();

                }
            });
            RequestQueue queue = Volley.newRequestQueue(ReviewTests.this);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private void callCODSuccess(Integer orderId, String uid) {

        showDialog();
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("a_u_id", uid);
            jsonObject.put("order_id", orderId.toString());

        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.DIAGONOSTIC_BASE_URL + ApiUrl.success, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();

                            Gson gson = new Gson();

                             successData = gson.fromJson(response.toString(), SuccessPaymentDetails.class);
                            if (successData.status == 1) {

                                Intent intent = new Intent(ReviewTests.this, TestBookingConfirm.class);

                                startActivity(intent);
                            } else {
                                showToast(successData.message);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();

                }
            });
            RequestQueue queue = Volley.newRequestQueue(ReviewTests.this);
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

    }
}
