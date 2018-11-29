package com.medarogya.appointment.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.medarogya.appointment.R;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.apis.MyService;
import com.medarogya.appointment.model.Details;
import com.medarogya.appointment.model.Login;
import com.medarogya.appointment.model.LoginResult;
import com.medarogya.appointment.utils.MessageToast;
import com.medarogya.appointment.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.edt_user_number)
    EditText edt_number;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.check_remember_me)
    TextView check_rememberme;
    @BindView(R.id.img_forgot_password)
    TextView forgot_password;
    @BindView(R.id.btn_sign_in)
    Button sign_in;
    @BindView(R.id.txt_sign_up)
    TextView sign_up;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ButterKnife.bind(this);
        sign_in.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
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

    private void SignIn() {
        String number=edt_number.getText().toString();
        String passworg=edt_password.getText().toString();
        if(validate(number,passworg))
        {
            Login login=new Login();
            login.setMobile(number);
            login.setPassword(passworg);
            if(isConnected())
            {
                  ApiCall(login);
            }
            else
               showToast("No Internet");

        }


    }

    private boolean validate(String number, String passworg) {
//        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(number).matches()){
//            edt_number.setError("enter a valid email address");
//            return false;
//        }
        if(number.length()<10)
        {
            edt_number.setError("enter a valid number");
            return false;
        }

        if(passworg.length()<6)
       {
           edt_password.setError("password should be 6 character");
           return false;
       }

      return true;
    }

    private void ApiCall(final Login login) {

        String token=getToken();
        if(token==null)
            token=manager.getSingleField(SessionManager.Token);
        if(token==null)
            token="null";
        login.setToken(token);
        MyService service=MyService.Factory.create(getApplicationContext());
        Call<LoginResult> call=service.login(login, ApiUrl.content_type);
         showDialog();
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                    hideDialog();
                    if(!response.isSuccessful())
                    {
                        showToast("Server side error");
                        return;
                    }
                LoginResult result=response.body();
                          showToast(result.getMessage());
                if(result.getStatus()==1)
                {
                    Details details=result.getDetails();

                    manager.createSigninSession(login,result,true);
                    startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                    finish();
                }


            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
            hideDialog();
            }
        });



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}

