package com.medarogya.appointment.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.medarogya.appointment.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class getSingleCardInfo extends BaseActivity
{
    @BindView(R.id.back)
    ImageView back;
String cardNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_single_card_info);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        cardNo=getIntent().getExtras().getString("cardNo");
        Toast.makeText(this, cardNo, Toast.LENGTH_SHORT).show();

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
