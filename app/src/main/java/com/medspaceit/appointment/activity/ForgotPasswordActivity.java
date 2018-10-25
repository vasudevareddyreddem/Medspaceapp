package com.medspaceit.appointment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.medspaceit.appointment.R;
import com.medspaceit.appointment.apis.ApiUrl;
import com.medspaceit.appointment.apis.MyService;
import com.medspaceit.appointment.model.RegResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener{


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.txt_sign_in)
    TextView signIn;

    @BindView(R.id.email)
    EditText edt_mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        submit.setOnClickListener(this);
        signIn.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                Intent intent=new Intent(ForgotPasswordActivity.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.submit:
                submit();

                break;
            case R.id.txt_sign_in:
                Intent i=new Intent(ForgotPasswordActivity.this, SignInActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
                break;



        }
    }

    private void submit() {
        String email=edt_mail.getText().toString();
        if(email==null||email.isEmpty()){
           edt_mail.setError("please enter email id");

            return;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edt_mail.setError("enter a valid email address");
            return ;
        }
        if(isConnected())
        {
            ApiCall(email);
        }
        else
       showToast("No Internet");
    }

    private void ApiCall(String email) {
        JsonObject object=new JsonObject();
        object.addProperty("email",email);
        MyService service= MyService.Factory.create(getApplicationContext());
        Call<RegResult> call=service.forgetPsw(object, ApiUrl.content_type);
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


                RegResult result=response.body();
                showToast(result.getMessage());

            }

            @Override
            public void onFailure(Call<RegResult> call, Throwable t) {
            hideDialog();
        }});



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(ForgotPasswordActivity.this, SignInActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
    }
}
