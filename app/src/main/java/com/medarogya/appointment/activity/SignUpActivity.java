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

    @BindView(R.id.password)
    EditText edt_password;

    @BindView(R.id.repassword)
    EditText edt_conform_password;

    @BindView(R.id.signup)
    Button signup;
    @BindView(R.id.txt_sign_in)
    TextView sign_in;
    static final Pattern CODE_PATTERN = Pattern.compile("([0-9]{0,4})|([0-9]{4}-)+|([0-9]{4}-[0-9]{0,4})+");

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



//        String cardNo=edt_cardno.getText().toString();
//        if(cardNo==null||cardNo.isEmpty()){
//            edt_cardno.setError("Plase enter cardNo");
//            return;
//        }
//        if(cardNo.length()<12) {
//            edt_cardno.setError("Plase enter valid card no.");
//            return;
//        }

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
            edt_mobile_num.setError("number have 10 numbers");
            return;
        }



        String password=edt_password.getText().toString();
        String conform_password=edt_conform_password.getText().toString();
        if(password==null||password.isEmpty()){
            edt_password.setError("enter password");
            return;
        }
        if(password.length()<6){
            edt_password.setError("password must be  mininum 6 charecters");
            return;
        }

        if(conform_password==null||conform_password.isEmpty()){
            edt_conform_password.setError("re-enter password");
            return;
        }
        if(!password.equals(conform_password)){
            edt_conform_password.setError("password and conform password are not same");
            return;
        }
        else {


        final Registration registration=new Registration();
        registration.setName(user_name);
        registration.setEmail(user_email);
        registration.setMobile(mobile_number);
       // registration.setCardno(cardNo);
        registration.setPassword(password);
        registration.setConfirmpassword(conform_password);
        String token=getToken();
      //  Log.e("token====",token);
        if(token==null)
            token=manager.getSingleField(SessionManager.Token);
        if(token==null)
            token="null";
        registration.setToken(token);
//        MyService service=MyService.Factory.create(getApplication());
        if(isConnected()) {
            Call<RegResult> call=service.registration(registration, ApiUrl.content_type);
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
