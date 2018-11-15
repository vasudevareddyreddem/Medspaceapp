package com.medspaceit.appointments.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.medspaceit.appointments.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CallToBookLabTest extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.btn_call_order)
    Button btn_call_order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_to_book_lab_test);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        btn_call_order.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
                case R.id.btn_call_order:
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:7997999108"));
                    startActivity(intent);
                break;
        }
    }
}
