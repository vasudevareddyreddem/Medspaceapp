package com.medspaceit.appointments.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.medspaceit.appointments.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewTests extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.txt_famount)
    TextView txt_famount;

    @BindView(R.id.paymeny_type)
    RadioGroup paymeny_type;

    @BindView(R.id.cod)
    RadioButton cod;


    @BindView(R.id.online_payment)
    RadioButton online_payment;


    @BindView(R.id.btn_paynow)
    Button btn_paynow;

    @BindView(R.id.your_state_progress_bar_id4)
    StateProgressBar your_state_progress_bar_id4;

    String patient_details_id,billing_id,passAmount;
    String paymentTypes = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_tests);
        ButterKnife.bind(this);

        back.setOnClickListener(this);
        btn_paynow.setOnClickListener(this);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        patient_details_id = b.getString("patient_details_id");
        billing_id = b.getString("billing_id");
        passAmount = b.getString("passAmount");
        txt_famount.setText("Total: ₹"+passAmount);

        paymeny_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                btn_paynow.setVisibility(View.VISIBLE);
                if (checkedId == R.id.online_payment) {
                    paymentTypes = "OnlinePayment";
                } else if (checkedId == R.id.cod) {
                    paymentTypes = "CashOnDelivery";
                } else {
                    paymentTypes = "";
                }

            }
        });



    }

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
