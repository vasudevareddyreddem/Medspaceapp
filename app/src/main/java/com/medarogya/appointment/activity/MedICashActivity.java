package com.medarogya.appointment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.medarogya.appointment.R;
import com.medarogya.appointment.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedICashActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.btn_submit_wallet)
    Button btn_submit_wallet;

    @BindView(R.id.wallet_type)
    RadioGroup wallet_type;

    @BindView(R.id.rb_OP)
    RadioButton rb_OP;

    @BindView(R.id.rb_lab)
    RadioButton rb_lab;

    @BindView(R.id.rb_IP)
    RadioButton rb_IP;

    String selectedType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_icash);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        btn_submit_wallet.setOnClickListener(this);

        wallet_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                btn_submit_wallet.setVisibility(View.VISIBLE);
                if (checkedId == R.id.rb_OP) {
                    selectedType = "1";
                } else if (checkedId == R.id.rb_IP) {
                    selectedType = "2";
                } else if (checkedId == R.id.rb_lab) {
                    selectedType = "3";
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_submit_wallet:
                String UID=manager.getSingleField(SessionManager.KEY_ID);
                if (isConnected()){
                if (selectedType.equals("1")) {
                    Intent intent=new Intent(this,OPActivity.class);
                    intent.putExtra("UID",UID);
                    startActivity(intent);
                }
                else if (selectedType.equals("2")) {
                    Intent intent=new Intent(this,IPActivity.class);
                    intent.putExtra("type","IP");
                    intent.putExtra("UID",UID);
                    startActivity(intent);

                }
                else if (selectedType.equals("3")) {
                    Intent intent=new Intent(this,IPActivity.class);

                    intent.putExtra("type","LAB");
                    intent.putExtra("UID",UID);
                    startActivity(intent);                }

                }
                else {
                    showToast(getString(R.string.nointernet));
                }


                break;


        }
    }
}
