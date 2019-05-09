package com.medarogya.appointment.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.medarogya.appointment.R;
import com.medarogya.appointment.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedICashHomeActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.hospital_card)
    CardView hospital_card;
    @BindView(R.id.wallet_history_card)
    CardView wallet_history_card;

    @BindView(R.id.pharamcy_outsource_card)
    CardView pharamcy_outsource_card;

    @BindView(R.id.carriers_jobsearch_card)
    CardView carriers_jobsearch_card;

    @BindView(R.id.labtests_card)
    CardView labtests_card;

    @BindView(R.id.freehealthcamp_card)
    CardView freehealthcamp_card;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_icash_home);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        hospital_card.setOnClickListener(this);
        wallet_history_card.setOnClickListener(this);
        pharamcy_outsource_card.setOnClickListener(this);
        labtests_card.setOnClickListener(this);
        freehealthcamp_card.setOnClickListener(this);
        carriers_jobsearch_card.setOnClickListener(this);
        UID = manager.getSingleField(SessionManager.KEY_ID);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.freehealthcamp_card:
                startActivity(new Intent(MedICashHomeActivity.this, HealthCampActivity.class));

                break;
            case R.id.carriers_jobsearch_card:
                startActivity(new Intent(MedICashHomeActivity.this, JobsListActivity.class));
                break;
            case R.id.labtests_card:
                startActivity(new Intent(MedICashHomeActivity.this, PharmacyLabTest.class));

                break;
            case R.id.pharamcy_outsource_card:
                startActivity(new Intent(MedICashHomeActivity.this, Pharamcy_Outsource_Activity.class));
                break;
            case R.id.hospital_card:
                startActivity(new Intent(MedICashHomeActivity.this, MedICashActivity.class));
                break;
            case R.id.wallet_history_card:
                Intent intent = new Intent(this, WalletHistory.class);
                intent.putExtra("UID", UID);
                startActivity(intent);
                break;
        }
    }
}
