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

public class Pharmacy extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.order_medicines_card)
    CardView order_medicines_card;
    @BindView(R.id.call2Order_card)
    CardView call2Order_card;

    @BindView(R.id.my_order_card)
    CardView my_order_card;

    @BindView(R.id.track_order_card)
    CardView track_order_card;


    String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        order_medicines_card.setOnClickListener(this);
        call2Order_card.setOnClickListener(this);
        my_order_card.setOnClickListener(this);
        track_order_card.setOnClickListener(this);
        UID=manager.getSingleField(SessionManager.KEY_ID);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.track_order_card:
                Intent i3=new Intent(this,PharmacyMyOrders.class);
                i3.putExtra("track","1");
                startActivity(i3);


                break;

            case R.id.my_order_card:
                Intent i4=new Intent(this,PharmacyMyOrders.class);
                i4.putExtra("track","0");
                startActivity(i4);

                break;
            case R.id.order_medicines_card:
                Intent i1=new Intent(this,OrderMedicines.class);
                i1.putExtra("UID",UID);
               startActivity(i1);
                break;
            case R.id.call2Order_card:
                Intent intent=new Intent(this,CallToBookLabTest.class);

                intent.putExtra("calls","1");
                startActivity(intent);
                break;
        }
    }
}

