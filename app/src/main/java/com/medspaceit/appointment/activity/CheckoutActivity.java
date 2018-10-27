package com.medspaceit.appointment.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.medspaceit.appointment.R;
import com.medspaceit.appointment.apis.ApiUrl;
import com.medspaceit.appointment.utils.MessageToast;
import com.medspaceit.appointment.utils.SessionManager;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckoutActivity extends BaseActivity implements PaymentResultListener {

    double total;

    private Button buttonConfirmOrder;
    private EditText editTextPayment;
    String name,email,phone,card_assign_number,heaith_card_no;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        findViews();
        listeners();
    }

    public void findViews() {
        buttonConfirmOrder = (Button) findViewById(R.id.buttonConfirmOrder);
        editTextPayment = (EditText) findViewById(R.id.editTextPayment);
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        name=b.getString("name");
        email=b.getString("email");
        phone=b.getString("phone");
        heaith_card_no=b.getString("heaith_card_no");
        card_assign_number=b.getString("card_assign_number");


    }

    public void listeners() {


        buttonConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextPayment.getText().toString().equals(""))
                {
                    Toast.makeText(CheckoutActivity.this, "Please fill payment", Toast.LENGTH_LONG).show();
                    return;
                }
                if (isConnected()) {

                    startPayment();
                    finish();
                } else showToast("No Internet");

            }
        });
    }


    public void startPayment() {
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", name);
            options.put("description", "Health Card Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");

            String payment = editTextPayment.getText().toString();

             total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", phone);

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(this, "Payment successfully done! " + razorpayPaymentID, Toast.LENGTH_SHORT).show();

        CallApi(razorpayPaymentID);
    }

    private void CallApi(String razorpayPaymentID) {

        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("a_u_id", manager.getSingleField(SessionManager.KEY_NAME));
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
                    ApiUrl.BaseUrl+ApiUrl.payment, new JSONObject(json),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            try {
                                JSONObject job=new JSONObject(String.valueOf(response));
                                Log.i("payment response====",response.toString());
                                String message=job.getString("message");
                                Toast.makeText(CheckoutActivity.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(CheckoutActivity.this, ""+response.toString(), Toast.LENGTH_SHORT).show();

                                    Intent intent=new Intent(CheckoutActivity.this,FinalScreen.class);
                                    intent.putExtra("name",name);
                                    intent.putExtra("phone",phone);
                                    intent.putExtra("heaith_card_no",heaith_card_no);
                                    startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//

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
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }

    @Override
    public void onClick(View v) {

    }
}