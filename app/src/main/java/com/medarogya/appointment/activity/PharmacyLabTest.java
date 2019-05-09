package com.medarogya.appointment.activity;

import android.annotation.SuppressLint;


import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.medarogya.appointment.R;
import com.medarogya.appointment.apis.ApiUrl;


import com.medarogya.appointment.model.RequestdataPojo;
import com.medarogya.appointment.model.ScanQRCodePojo;
import com.medarogya.appointment.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;



import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class PharmacyLabTest extends BaseActivity implements ZXingScannerView.ResultHandler {
    @BindView(R.id.pl_city_ll)
    LinearLayout pl_city_ll;
    @BindView(R.id.pl_city_txt)
    TextView pl_city_txt;

    @BindView(R.id.pl_pharmacy_ll)
    LinearLayout pl_pharmacy_ll;
    @BindView(R.id.pl_pharmacy_txt)
    TextView pl_pharmacy_txt;

    @BindView(R.id.pl_total_amount)
    EditText pl_total_amount;
    @BindView(R.id.pharmacylab_scrollview)
    ScrollView pharmacylab_scrollview;


    @BindView(R.id.btn_pl_scan)
    Button btn_pl_scan;
    @BindView(R.id.pl_pay_amount)
    TextView pl_pay_amount;
    @BindView(R.id.btn_pl_submit)
    Button btn_pl_submit;

    @BindView(R.id.cvpharmecylab)
    CardView cvpharmecylab;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.pl_ll_for_fmount)
    LinearLayout pl_ll_for_fmount;

    String city, pharmacyid, disAmt;
    String UID;

    private ZXingScannerView mScannerView;
    private ViewGroup pl_content_frame;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_lab_test);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        btn_pl_scan.setOnClickListener(this);
        btn_pl_submit.setOnClickListener(this);
        back.setOnClickListener(this);


        UID = manager.getSingleField(SessionManager.KEY_ID);
        pl_content_frame = (ViewGroup) findViewById(R.id.pl_content_frame);
        mScannerView = new ZXingScannerView(this);
        hideSoftKeyboard();


    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        showDialog();
        callQrCodeApi(rawResult.getText());
    }

    private void callQrCodeApi(String qr_code) {

        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("total_amt", pl_total_amount.getText().toString());
            jsonObject.put("qr_code", qr_code);
            //Log.e("======lab>>>",jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        json = jsonObject.toString();
        JsonObjectRequest jsonObjectRequest = null;

        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.PHARMACYUrl + ApiUrl.scan_qr_code, new JSONObject(json), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    hideDialog();
                    Gson gson = new Gson();
                    ScanQRCodePojo data = gson.fromJson(response.toString(), ScanQRCodePojo.class);
                    if (data.status.toString().trim().equals("1")) {
                        pl_city_txt.setText(data.details.city);
                        pl_pharmacy_txt.setText(data.details.name);
                        pl_pay_amount.setText(data.totalAmt);
                        disAmt = data.disAmt;
                        pharmacyid = data.details.a_id;
                        pl_total_amount.setText("");

                        pl_content_frame.setVisibility(View.GONE);
                        pharmacylab_scrollview.setVisibility(View.VISIBLE);
                        cvpharmecylab.setVisibility(View.VISIBLE);
                        pl_ll_for_fmount.setVisibility(View.VISIBLE);
                        pl_total_amount.setCursorVisible(false);

                    } else {
                        showToast(data.message);
                        mScannerView.stopCamera();
                        pl_content_frame.setVisibility(View.GONE);
                        pharmacylab_scrollview.setVisibility(View.VISIBLE);
                        cvpharmecylab.setVisibility(View.VISIBLE);
                        pl_ll_for_fmount.setVisibility(View.GONE);

                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                    Log.e("====error", error.getMessage());

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            hideDialog();
            Log.e("====error1", e.getMessage());
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pl_scan:
                hideSoftKeyboard();
                String pta = pl_total_amount.getText().toString();
                char c = 0;
                if (pta.equals(""))
                {
                    showToast("Please select amount");
                } else if (pta != "") {
                    c = pta.charAt(0);

                    if (c == '0') {
                        Toast.makeText(PharmacyLabTest.this, "Please enter valid amount", Toast.LENGTH_SHORT).show();


                } else  if (mScannerView != null) {

                        if (mScannerView.getParent() != null) {
                            ((ViewGroup)
                                    mScannerView.getParent()).removeView(mScannerView); // to
                        }
                        pl_content_frame.addView(mScannerView);
                        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
                        mScannerView.startCamera();
                        pharmacylab_scrollview.setVisibility(View.GONE);
                        cvpharmecylab.setVisibility(View.GONE);
                        pl_content_frame.setVisibility(View.VISIBLE);
                    }
                }

                break;
            case R.id.back:
                finish();
                break;

            case R.id.btn_pl_submit:
                if (isConnected()) {
                    showDialog();
                    callInsertPayDetails();
                } else {
                    showToast("No Internet Connection");
                }

                break;
        }

    }


    private void callInsertPayDetails() {
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", UID);
            jsonObject.put("phar", pharmacyid);
            jsonObject.put("dis_amt", disAmt);
            jsonObject.put("amt", pl_pay_amount.getText().toString());
            Log.e("=====>  ", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json = jsonObject.toString();
        JsonObjectRequest jsonObjectRequest = null;

        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.PHARMACYUrl + ApiUrl.ins_pay_det, new JSONObject(json), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    hideDialog();
                    Gson gson = new Gson();
                    RequestdataPojo data = gson.fromJson(String.valueOf(response), RequestdataPojo.class);
                    if (data.status == 1) {
                        showToast(data.message);
                        finish();
                    } else {
                        showToast(data.message);
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

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
