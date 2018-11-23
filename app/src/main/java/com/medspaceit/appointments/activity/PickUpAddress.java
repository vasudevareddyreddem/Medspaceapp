package com.medspaceit.appointments.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.medspaceit.appointments.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickUpAddress extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.labelSpinner)
    Spinner labelSpinner;

    @BindView(R.id.et_home_address)
    EditText et_home_address;

    @BindView(R.id.et_landmark)
    EditText et_landmark;

    @BindView(R.id.et_city)
    EditText et_city;

    @BindView(R.id.et_pincode)
    EditText et_pincode;


    @BindView(R.id.btn_next_c)
    Button btn_next_c;
    @BindView(R.id.your_state_progress_bar_id3)
    StateProgressBar your_state_progress_bar_id3;

    String patient_details_id;
    String[] arraySpinner = new String[] {
            "Select", "Home", "Work", "Other"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up_address);
        ButterKnife.bind(this);
        Intent i = getIntent();
        Bundle b = i.getExtras();

        //patient_details_id = b.getString("patient_details_id");
        back.setOnClickListener(this);
        btn_next_c.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        labelSpinner.setAdapter(adapter);


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


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }}
