package com.medspaceit.appointment.activity;



import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.medspaceit.appointment.R;
import com.medspaceit.appointment.apis.ApiUrl;
import com.medspaceit.appointment.model.Profile;
import com.medspaceit.appointment.model.RegResult;
import com.medspaceit.appointment.utils.SessionManager;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.profile_pic)
    CircleImageView profile_pic;

    @BindView(R.id.user_name)
    EditText userName;

    @BindView(R.id.molibe_nmbr)
    EditText mobileNumber;

    @BindView(R.id.email)
    EditText mailId;

    @BindView(R.id.update_profile)
    Button updateProfile;

    @BindView(R.id.back)
    ImageView back;

      Profile profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setValues();

         back.setOnClickListener(this);
         updateProfile.setOnClickListener(this);

    }

    private void setValues() {
        profile=manager.getProfile();
        if(profile!=null){
        mailId.setText(profile.getMail());
        mobileNumber.setText(profile.getMobile());
        userName.setText(profile.getName());
        }


        String filename=manager.getSingleField(SessionManager.PROFILE_IMG_URL)+manager.getSingleField(SessionManager.PROFILE_IMG_PATH);
        File file=new File(filename);
        if(!file.exists()) {
            Glide.with(this)
                    .load(filename)
                    .error(R.drawable.dummy_user)
                    .into(profile_pic);
        }else
            {
            Glide.with(this)
                    .load(file)
                    .error(R.drawable.dummy_user)
                    .into(profile_pic);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back:
                onBackPressed();
                break;
            case R.id.update_profile:
                updateProfile();

                break;
        }
    }

    private void updateProfile() {

        if(validate()){
            if(isConnected())
            {
                showDialog();
                apiCall(profile);
            }
            else showToast("No Internet");
        }
    }

    private void apiCall(final Profile profile) {
        Call<RegResult> call=service.updateProfile(profile, ApiUrl.content_type);
        call.enqueue(new Callback<RegResult>() {
            @Override
            public void onResponse(Call<RegResult> call, Response<RegResult> response) {
              hideDialog();
              RegResult result=response.body();
              showToast(result.getMessage());
              if(result.getStatus()==1){
                 manager.createEditProfileSession(profile);
              }
            }

            @Override
            public void onFailure(Call<RegResult> call, Throwable t) {
            hideDialog();
            }
        });
    }

    public boolean validate() {
       String name=userName.getText().toString();
       String mobile=mobileNumber.getText().toString();
       String mail=mailId.getText().toString();
        if(name==null||name.isEmpty())
        {
            userName.setError("Enter name");
            return false;
        }
        if(mobile==null||mobile.isEmpty())
        {
            mobileNumber.setError("Enter number");
            return false;
        }
        if(mobile.length()<10)
        {
            mobileNumber.setError("Must 10 numbers");
            return false;
        }
        if(mail==null||mail.isEmpty())
        {
            mailId.setError("Enter mail");
            return false;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            mailId.setError("enter a valid email address");
            return false;
        }

        profile.setName(userName.getText().toString());
        profile.setMobile(mobileNumber.getText().toString());
        profile.setMail(mailId.getText().toString());

        return true;
    }



    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        super.onBackPressed();


    }
}
