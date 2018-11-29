package com.medarogya.appointment.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.medarogya.appointment.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerService extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.callButton)
    ImageView callButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        callButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.back:
                finish();
                break;

            case R.id.callButton:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:7997999108"));
                startActivity(intent);
                break;
        }


    }
}
