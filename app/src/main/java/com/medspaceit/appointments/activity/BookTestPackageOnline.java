package com.medspaceit.appointments.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_test_package_online);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
