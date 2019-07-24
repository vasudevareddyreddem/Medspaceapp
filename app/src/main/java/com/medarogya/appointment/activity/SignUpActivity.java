package com.medarogya.appointment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.medarogya.appointment.R;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.RegResult;
import com.medarogya.appointment.model.Registration;
import com.medarogya.appointment.utils.SessionManager;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.user_name)
    EditText edt_user_name;

    @BindView(R.id.user_email)
    EditText edt_user_email;

    @BindView(R.id.molibe_nmbr)
    EditText edt_mobile_num;

    @BindView(R.id.cardno)
    EditText edt_cardno;

    @BindView(R.id.user_city)
    EditText user_city;

    @BindView(R.id.signup)
    Button signup;
    @BindView(R.id.txt_sign_in)
    TextView sign_in;
    static final Pattern CODE_PATTERN = Pattern.compile("([0-9]{0,4})|([0-9]{4}-)+|([0-9]{4}-[0-9]{0,4})+");
String  token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        signup.setOnClickListener(this);
        sign_in.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signup:
                signUpUser();
                break;
            case R.id.txt_sign_in:
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
                break;
        }

    }

    private void signUpUser() {


        String user_name=edt_user_name.getText().toString();
        if(user_name==null||user_name.isEmpty()){
            edt_user_name.setError("Please Enter username");
            return;
        }

        String user_email=edt_user_email.getText().toString();
        if(user_email==null||user_email.isEmpty()){
            edt_user_email.setError("Please Enter username");
            return;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(user_email).matches()){
            edt_user_email.setError("enter a valid email address");
            return ;
        }
        String mobile_number=edt_mobile_num.getText().toString();
        if(mobile_number==null||mobile_number.isEmpty()){
            edt_mobile_num.setError("Please enter mobile number");
            return;
        }
        if(mobile_number.length()<10){
            edt_mobile_num.setError("Mobile number should be 10 digits");
            return;
        }



        String city=user_city.getText().toString();
        if(city==null||city.isEmpty()){
            user_city.setError("enter password");
            return;
        }
        else {

        final Registration registration=new Registration();
        registration.setName(user_name);
        registration.setEmail(user_email);
        registration.setMobile(mobile_number);
        registration.setCity(city);
         token=getToken();
      //  Log.e("token====",token);
        if(token==null)
            token=manager.getSingleField(SessionManager.Token);
        if(token==null)
            token="null";
        registration.setToken(token);
//        MyService service=MyService.Factory.create(getApplication());
        if(isConnected()) {
            Call<RegResult> call=newloginservice.registration(registration, ApiUrl.content_type);
            showDialog();
            call.enqueue(new Callback<RegResult>() {
                @Override
                public void onResponse(Call<RegResult> call, Response<RegResult> response) {
                 hideDialog();
                    if(!response.isSuccessful())
                    {
                        showToast("Server side error");
                        return;
                    }


                    RegResult body = response.body();
                    showToast(body.getMessage());
                      if( body.getStatus()==1)
                        {
                            manager.createSignUpSession(registration,body);
                            startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                            finish();
                        }
                }

                @Override
                public void onFailure(Call<RegResult> call, Throwable t) {
                 hideDialog();
                }
            });
        }
        else
        {
          showToast("No Internet");
        }


    }}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
