package com.medarogya.appointment.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.medarogya.appointment.Fragmant.CarrierFragment;
import com.medarogya.appointment.Fragmant.NotificationFragment;
import com.medarogya.appointment.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Carriers_Jobsearch_Activity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;

    Button carrier_btn, notification_btn;
    android.app.FragmentTransaction fragmentTransaction;
    boolean isActive=true;
    private  CarrierFragment fragment_carrier;
    private NotificationFragment fragment_notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carriers__jobsearch_);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        carrier_btn=findViewById(R.id.carrier_btn);
        notification_btn=findViewById(R.id.notification_btn);


        notification_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if(isActive)
                //  Toast.makeText(MainActivity.this, "one", Toast.LENGTH_SHORT).show();
                {
                    notification_btn.setBackgroundColor(Color.WHITE);
                    notification_btn.setTextColor(Color.BLACK);
                    carrier_btn.setBackgroundColor(getResources().getColor(R.color.accept));
                    carrier_btn.setTextColor(Color.WHITE);
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    //fragmentTransaction.setCustomAnimations(R.anim.lefttoright, R.anim.right_to_left);
                    fragment_notification = new NotificationFragment(); // CreateNewNote is fragment you want to display
                    fragmentTransaction.replace(R.id.frameLayoutContainer, fragment_notification);  // content_fragment is id of FrameLayout(XML file) where fragment will be displayed
                    fragmentTransaction.commit();}
                isActive=false;
                // hide current fragment

            }
        });

        carrier_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "two", Toast.LENGTH_SHORT).show();
                if(!isActive){

                    carrier_btn.setBackgroundColor(Color.WHITE);
                    carrier_btn.setTextColor(Color.BLACK);
                    notification_btn.setBackgroundColor(getResources().getColor(R.color.accept));
                    notification_btn.setTextColor(Color.WHITE);
                    fragmentTransaction = getFragmentManager().beginTransaction();
                   // fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit);
                    fragment_carrier = new CarrierFragment(); // CreateNewNote is fragment you want to display
                    fragmentTransaction.replace(R.id.frameLayoutContainer, fragment_carrier);
                    fragmentTransaction.commit();
                }
                isActive=true;
            }
        });
    }


    @Override
    public void onClick(View v) {

            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;



        }
    }
}
