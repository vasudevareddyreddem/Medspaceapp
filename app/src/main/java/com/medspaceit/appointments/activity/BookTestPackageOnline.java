package com.medspaceit.appointments.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.medspaceit.appointments.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookTestPackageOnline extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;

     @BindView(R.id.finalTestRecyclerView)
     RecyclerView finalTestRecyclerView;

     @BindView(R.id.totalTestTxt)
     TextView totalTestTxt;

     @BindView(R.id.txt_total_amount)
     TextView txt_total_amount;

     @BindView(R.id.txt_pickup_time)
     TextView txt_pickup_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_test_package_online);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        txt_total_amount.setOnClickListener(this);
        txt_pickup_time.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.txt_pickup_time:
                startActivity(new Intent(this, PickTimeSlot.class));
                break;
    }}
}
