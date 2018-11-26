package com.medspaceit.appointments.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.medspaceit.appointments.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestBookingConfirm extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.pt_name)
    TextView pt_name;

    @BindView(R.id.pt_mobile)
    TextView pt_mobile;

    @BindView(R.id.pt_email)
    TextView pt_email;

    @BindView(R.id.pt_gender)
    TextView pt_gender;

    @BindView(R.id.pt_age)
    TextView pt_age;

    @BindView(R.id.pt_datetime)
    TextView pt_datetime;

    @BindView(R.id.pt_address)
    TextView pt_address;

    @BindView(R.id.pt_landmark)
    TextView pt_landmark;

    @BindView(R.id.pt_locality)
    TextView pt_locality;

    @BindView(R.id.pt_pincode)
    TextView pt_pincode;

    @BindView(R.id.pt_time)
    TextView pt_time;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_booking_confirm);
        ButterKnife.bind(this);

        back.setOnClickListener(this);


        pt_name.setText(ReviewTests.successData.patientDetails.name);
        pt_mobile.setText(ReviewTests.successData.patientDetails.mobile);
        pt_email.setText(ReviewTests.successData.patientDetails.email);
        pt_gender.setText(ReviewTests.successData.patientDetails.gender);
        pt_age.setText(ReviewTests.successData.patientDetails.age);
        pt_datetime.setText(ReviewTests.successData.patientDetails.date);
        pt_time.setText(ReviewTests.successData.patientDetails.time);
        pt_address.setText(ReviewTests.successData.addressDetails.address);
        pt_landmark.setText(ReviewTests.successData.addressDetails.landmark);
        pt_locality.setText(ReviewTests.successData.addressDetails.locality);
        pt_pincode.setText(ReviewTests.successData.addressDetails.pincode);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                startActivity(new Intent(TestBookingConfirm.this, HomeActivity.class));
                this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);



                break;
    }}
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(TestBookingConfirm.this, HomeActivity.class));
        this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }
}
