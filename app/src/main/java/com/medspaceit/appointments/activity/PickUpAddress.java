package com.medspaceit.appointments.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.medspaceit.appointments.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickUpAddress extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.btn_next_c)
    Button btn_next_c;
    @BindView(R.id.your_state_progress_bar_id3)
    StateProgressBar your_state_progress_bar_id3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up_address);
        ButterKnife.bind(this);

        back.setOnClickListener(this);
        btn_next_c.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                break;
            case R.id.btn_next_c:
                Intent i=new Intent(this,ReviewTests.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                break;
//            case R.id.btn_next_a:
//                Intent i=new Intent(this,PatientDetails.class);
//                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }}
