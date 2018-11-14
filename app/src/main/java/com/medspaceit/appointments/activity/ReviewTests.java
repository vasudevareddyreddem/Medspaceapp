package com.medspaceit.appointments.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.medspaceit.appointments.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewTests extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;


    @BindView(R.id.btn_paynow)
    Button btn_paynow;

    @BindView(R.id.your_state_progress_bar_id4)
    StateProgressBar your_state_progress_bar_id4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_tests);
        ButterKnife.bind(this);

        back.setOnClickListener(this);
        btn_paynow.setOnClickListener(this);    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                break;

            case R.id.btn_paynow:

                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }
}
