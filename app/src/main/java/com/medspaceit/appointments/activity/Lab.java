package com.medspaceit.appointments.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.medspaceit.appointments.R;
import com.medspaceit.appointments.apis.ApiUrl;
import com.medspaceit.appointments.model.City;
import com.medspaceit.appointments.model.CityList;
import com.medspaceit.appointments.model.Formatter;
import com.medspaceit.appointments.utils.MessageToast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Lab extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;


    @BindView(R.id.select_labtest_ll)
    LinearLayout select_labtest_ll;
    @BindView(R.id.select_booklabtest_ll)
    LinearLayout select_booklabtest_ll;
    @BindView(R.id.select_myreport_ll)
    LinearLayout select_myreport_ll;
    @BindView(R.id.checkup_recyclerview)
    RecyclerView checkup_recyclerview;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        select_labtest_ll.setOnClickListener(this);
        select_booklabtest_ll.setOnClickListener(this);
        select_myreport_ll.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.select_myreport_ll:
                startActivity(new Intent(this, MyReports.class));
                break;
            case R.id.select_labtest_ll:
                startActivity(new Intent(this, SelectLabTest.class));
                break;
            case R.id.select_booklabtest_ll:
                startActivity(new Intent(this, CallToBookLabTest.class));
                break;

        }
    }



}
