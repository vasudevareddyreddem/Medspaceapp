package com.medarogya.appointment.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.medarogya.appointment.R;
import com.medarogya.appointment.adapters.AcceptAdapter;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.apis.MyService;
import com.medarogya.appointment.apis.MyServiceNewLogin;
import com.medarogya.appointment.model.AcceptListPJ;
import com.medarogya.appointment.model.Details;
import com.medarogya.appointment.model.Login;
import com.medarogya.appointment.model.LoginResult;
import com.medarogya.appointment.model.OtpResult;
import com.medarogya.appointment.utils.MessageToast;
import com.medarogya.appointment.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.edt_user_number)
    EditText edt_number;
    @BindView(R.id.edt_user_otp)
    EditText edt_user_otp;
    @BindView(R.id.check_remember_me)
    TextView check_rememberme;
    @BindView(R.id.img_forgot_password)
    TextView forgot_password;
    @BindView(R.id.btn_sign_in)
    Button sign_in;
    @BindView(R.id.btn_submit_otp)
    Button btn_submit_otp;
    @BindView(R.id.txt_sign_up)
    TextView sign_up;
    @BindView(R.id.tv_resend_otp)
    TextView tv_resend_otp;
    @BindView(R.id.ll_login)
    LinearLayout ll_login;
    @BindView(R.id.ll_otp)
    LinearLayout ll_otp;
String userId="";
 String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ButterKnife.bind(this);
        sign_in.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
        tv_resend_otp.setOnClickListener(this);
        btn_submit_otp.setOnClickListener(this);

//        if(manager.isRemembered())
//        {
//            edt_number.setText(manager.getSingleField(SessionManager.KEY_EMAIL));
//            edt_password.setText(manager.getSingleField(SessionManager.KEY_PASSWORD));
//            check_rememberme.setChecked(true);
//
//        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sign_in:
                SignIn();
                break;
                case R.id.btn_submit_otp:
                    String otp=edt_user_otp.getText().toString().trim();
                    if(otp.equals(""))
                    {
                        showToast("Please enter otp");
                    }
                    else if(otp.length()<6)
                    {
                        showToast("Please enter valid otp");
                    }
                    else {
                        if(isConnected())
                        CheckOTP(otp);
                        else showToast("No Internet Connection Found");
                    }
                break;
                case R.id.tv_resend_otp:
                    if(isConnected())
                    {
                  edt_user_otp.setText("");
                        ResendOTP();}
                    else {showToast("No Internet Connection Found");}
                break;
            case R.id.txt_sign_up:
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
                finish();
                break;
            case R.id.img_forgot_password:
                startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
                finish();
                break;

        }

    }

    private void ResendOTP() {
        showDialog();
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", userId);
            jsonObject.put("token", token);
            Log.e("=====3 rd=====",jsonObject.toString());


        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.NewLoginBaseUrl+ApiUrl.resend_otp, new JSONObject(json),
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();

                        }
                    }, new com.android.volley.Response.ErrorListener() {
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

    private void CheckOTP(String otp) {
        showDialog();
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", userId);
            jsonObject.put("otp", otp);
            jsonObject.put("token", token);
            Log.e("=====2 nd=====",jsonObject.toString());


        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.NewLoginBaseUrl+ApiUrl.login_otp_verify, new JSONObject(json),
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Gson gson=new Gson();
                            OtpResult data= gson.fromJson(String.valueOf(response),OtpResult.class);


                            if(data.status.equals(1))
                            {
                                showToast(data.message);
                                manager.createSigninSession(data,true);
                                startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                                finish();
                            }
                            else {
                                showToast(data.message);
                                tv_resend_otp.setVisibility(View.VISIBLE);
                            }

                        }
                    }, new com.android.volley.Response.ErrorListener() {
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

    private void SignIn() {
        String number=edt_number.getText().toString();
        if(number.equals(""))
        {
            showToast("Please enter mobile no.");
        }
        else if(number.length()<10)
        {
            showToast("Please enter valid mobile no.");
        }
        else {
            Login login=new Login();
            login.setMobile(number);
            if(isConnected())
            {
                  ApiCall(login);
            }
            else
               showToast("No Internet Connection Found");

        }


    }



    private void ApiCall(final Login login) {

         token=getToken();
        if(token==null)
            token=manager.getSingleField(SessionManager.Token);
        if(token==null)
            token="null";
        login.setToken(token);
        showDialog();
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile",login.getMobile());
            jsonObject.put("token",token);
            Log.e("=====1 st=====",jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.NewLoginBaseUrl+ApiUrl.login, new JSONObject(json),
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Gson gson=new Gson();
                            LoginResult data=gson.fromJson(String.valueOf(response),LoginResult.class);

                        if(data.status.equals(0))
                            {
                                showToast(data.message);
                            }
                            else {
                            hideSoftKeyboard();

                            ll_login.setVisibility(View.GONE);
                                ll_otp.setVisibility(View.VISIBLE);
                                userId=data.userId.aUId;
                                hideSoftKeyboard();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
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
        super.onBackPressed();
        finishAffinity();
    }
    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }}


