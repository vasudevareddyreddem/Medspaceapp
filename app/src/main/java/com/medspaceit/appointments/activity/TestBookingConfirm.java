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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_booking_confirm);
        ButterKnife.bind(this);

        back.setOnClickListener(this);

        Bundle b = getIntent().getExtras();

        String dat = b.getString("dat");
        String tim = b.getString("tim");
        String Name = b.getString("Name");
        String Mobile = b.getString("Mobile");
        String Email = b.getString("Email");
        String Gender = b.getString("Gender");
        String Age = b.getString("Age");

        String Address = b.getString("Address");
        String Landmark = b.getString("Landmark");
        String Locality = b.getString("Locality");
        String Zipcode = b.getString("Zipcode");

        pt_name.setText("Name:      "+Name);
        pt_mobile.setText("Mobile :      "+Mobile);
        pt_email.setText("Email ID:      "+Email);
        pt_gender.setText("Gender:      "+Gender);
        pt_age.setText("Age:      "+Age);
        pt_datetime.setText("Sample Pickup Date Time: "+dat+" "+tim);
        pt_address.setText("Address:      "+Address);
        pt_landmark.setText("Landmark:      "+Landmark);
        pt_locality.setText("Locality:      "+Locality);
        pt_pincode.setText("Zipcode:      "+Zipcode);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                startActivity(new Intent(TestBookingConfirm.this, HomeActivity.class));

                break;
    }}
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(TestBookingConfirm.this, HomeActivity.class));
    }
}
